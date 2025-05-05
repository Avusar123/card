package com.card_management.transaction.infrastructure.saga.listener;

import com.card_management.shared.dto.TransactionStatus;
import com.card_management.shared.kafka.Listener;
import com.card_management.shared.kafka.event.TransactionSagaResponse;
import com.card_management.transaction.infrastructure.TransactionRepo;
import com.card_management.transaction.infrastructure.redis.TransactionDocumentService;
import com.card_management.transaction.infrastructure.saga.TransactionDocument;
import com.card_management.transaction.infrastructure.saga.TransactionSagaStep;
import com.card_management.transaction.infrastructure.saga.producer.TransactionSagaGeneralProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Listener
public class TransactionSagaListener {
    private final TransactionDocumentService documentService;
    private final TransactionSagaFinisher finisher;
    private final TransactionSagaGeneralProducer producer;
    private final TransactionRepo repo;

    @Autowired
    public TransactionSagaListener(TransactionDocumentService documentService,
                                   TransactionSagaFinisher finisher,
                                   TransactionSagaGeneralProducer producer,
                                   TransactionRepo repo) {
        this.documentService = documentService;
        this.finisher = finisher;
        this.producer = producer;
        this.repo = repo;
    }

    @KafkaListener(topics = "transaction-saga-response", groupId = "transaction")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void listen(TransactionSagaResponse response, Acknowledgment ack) {
        var transaction = repo.findById(response.id());

        if (!documentService.exists(response.id()) || transaction.isEmpty() || transaction.get().getStatus() != TransactionStatus.PROCESSING) {
            return;
        }

        var document = documentService.get(response.id());

        if (response.failureReason().isPresent()) {
            document.setFailureReason(response.failureReason().get());
        }

        switch (document.getSagaStep()) {
            case LIMITS_CHECK -> {
                if (response.serviceName().equals("limit"))
                    handleLimitsCheck(document);
            }
            case MONEY_TRANSFER -> {
                if (response.serviceName().equals("card"))
                    handleMoneyTransfer(document);
            }
        }

        ack.acknowledge();
    }

    public void handleLimitsCheck(TransactionDocument document) {
        if (document.getFailureReason().isPresent()) {
            finisher.failure(document.getId(), document.getFailureReason().get());
            documentService.delete(document.getId());
        } else {
            document.setSagaStep(TransactionSagaStep.MONEY_TRANSFER);

            producer.produce(document);

            documentService.save(document);
        }
    }

    public void handleMoneyTransfer(TransactionDocument document) {
        if (document.getFailureReason().isPresent()) {
            handleLimitsCheck(document);
        } else {
            try {
                finisher.success(document.getId());
            } catch (NoSuchElementException ex) {
                finisher.forceRelease(document.getMiniDto().from(), document.getMiniDto().to());
            }

            documentService.delete(document.getId());
        }
    }
}

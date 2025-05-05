package com.card_management.transaction.infrastructure.saga.producer;

import com.card_management.shared.dto.TransactionMiniDto;
import com.card_management.shared.id.TransactionId;
import com.card_management.transaction.infrastructure.redis.RedisCardMutex;
import com.card_management.transaction.infrastructure.redis.TransactionDocumentService;
import com.card_management.transaction.infrastructure.saga.TransactionDocument;
import com.card_management.transaction.infrastructure.saga.TransactionSagaStep;
import com.card_management.transaction.infrastructure.saga.listener.TransactionSagaFinisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TransactionSagaEntryPoint {
    private final TransactionDocumentService documentService;
    private final RedisCardMutex mutex;
    private final TransactionSagaGeneralProducer coordinator;
    private final TransactionSagaFinisher finisher;

    @Autowired
    public TransactionSagaEntryPoint(TransactionDocumentService documentService,
                                     RedisCardMutex mutex,
                                     TransactionSagaGeneralProducer coordinator,
                                     TransactionSagaFinisher finisher) {
        this.documentService = documentService;
        this.mutex = mutex;
        this.coordinator = coordinator;
        this.finisher = finisher;
    }

    public void handleNew(TransactionMiniDto dto) {
        var document = new TransactionDocument(dto);

        try {
            mutex.lock(document.getMiniDto().from(), dto.id());
            mutex.lock(document.getMiniDto().to(), dto.id());

            document.setSagaStep(TransactionSagaStep.LIMITS_CHECK);

            coordinator.produce(document);

            documentService.save(document);
        } catch (Exception ex) {
            mutex.release(document.getMiniDto().from());
            mutex.release(document.getMiniDto().to());

            throw ex;
        }

    }

    public boolean isAlreadyInProgress(TransactionId id) {
        return documentService.exists(id);
    }

    public boolean isEmpty(TransactionMiniDto transactionDto) {
        return mutex.empty(transactionDto.from()) && mutex.empty(transactionDto.to());
    }

    public void handleExisting(TransactionId id) {
        var document = documentService.get(id);

        if (Duration.between(document.getMiniDto().addedTime(), LocalDateTime.now()).toMinutes() >= 30) {
            finisher.failure(id, "Timeout exception");
        } else if (Duration.between(document.getLastAttemptTime(), LocalDateTime.now()).toMinutes() >= 10) {
            coordinator.produce(document);
            document.setLastAttemptTime(LocalDateTime.now());
            documentService.save(document);
        }
    }
}

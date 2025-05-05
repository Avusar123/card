package com.card_management.transaction.infrastructure.saga.producer;

import com.card_management.shared.TimeRange;
import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.request.LimitsCheckSagaRequest;
import com.card_management.shared.kafka.request.TransferSagaRequest;
import com.card_management.transaction.infrastructure.TransactionRepo;
import com.card_management.transaction.infrastructure.saga.TransactionDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Producer
public class TransactionSagaGeneralProducer {
    private final LimitsCheckRequestProducer limitsCheckRequestProducer;
    private final TransferRequestProducer transferRequestProducer;
    private final TransactionRepo repo;

    @Autowired
    public TransactionSagaGeneralProducer(LimitsCheckRequestProducer limitsCheckRequestProducer,
                                          TransferRequestProducer transferRequestProducer,
                                          TransactionRepo repo) {
        this.limitsCheckRequestProducer = limitsCheckRequestProducer;
        this.transferRequestProducer = transferRequestProducer;
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public void produce(TransactionDocument document) {
        switch (document.getSagaStep()) {
            case LIMITS_CHECK -> produceCheck(document);
            case MONEY_TRANSFER -> produceTransfer(document);
        }
    }

    private void produceCheck(TransactionDocument document) {
        Map<TimeRange, Integer> sums = new HashMap<>();

        for (var range : TimeRange.values()) {
            sums.put(range, repo
                    .findOutcomeSumInPeriod(
                            document.getMiniDto().from(),
                            LocalDateTime.now().minusDays(range.getDays())).orElse(0)
            );
        }

        limitsCheckRequestProducer.send(new LimitsCheckSagaRequest(
                document.getMiniDto(),
                sums
        ));
    }

    private void produceTransfer(TransactionDocument document) {
        var request = new TransferSagaRequest(document.getMiniDto());

        transferRequestProducer.send(request);
    }
}

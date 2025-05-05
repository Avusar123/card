package com.card_management.limit.application.listener;

import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.kafka.Listener;
import com.card_management.shared.kafka.TransactionResponseProducer;
import com.card_management.shared.kafka.event.TransactionSagaResponse;
import com.card_management.shared.kafka.request.LimitsCheckSagaRequest;
import com.card_management.shared.redis.ResponseCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Listener
public class SagaListener {

    private final LimitRepo repo;
    private final ResponseCacheService cacheService;
    private final TransactionResponseProducer producer;

    private static final String SERVICE_NAME = "limit";

    @Autowired
    public SagaListener(LimitRepo repo,
                        ResponseCacheService cacheService,
                        TransactionResponseProducer producer) {
        this.repo = repo;
        this.cacheService = cacheService;
        this.producer = producer;
    }

    @Transactional(readOnly = true)
    @KafkaListener(topics = "limits-check-saga", groupId = "limit")
    public void listen(LimitsCheckSagaRequest request, Acknowledgment ack) {
        var transaction = request.dto();

        TransactionSagaResponse response;

        if (cacheService.cached(SERVICE_NAME, transaction.id())) {
            response = cacheService.get(SERVICE_NAME, transaction.id());
        } else {
            var limits = repo.findAllByCardId(transaction.from());

            if (limits
                    .stream()
                    .anyMatch(
                            limit -> (
                                    request
                                        .sums()
                                        .get(limit.getId().range()) + transaction.amount()) > limit.getMaxAmount())) {
                response = new TransactionSagaResponse(transaction.id(), Optional.of("Source card hit the limits"), SERVICE_NAME);
            } else {
                response = new TransactionSagaResponse(transaction.id(), Optional.empty(), SERVICE_NAME);
            }

            cacheService.setCache(SERVICE_NAME, transaction.id(), response);
        }

        producer.send(response);

        ack.acknowledge();
    }
}

package com.card_management.card.application.listener;

import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.kafka.Listener;
import com.card_management.shared.kafka.TransactionResponseProducer;
import com.card_management.shared.kafka.event.TransactionSagaResponse;
import com.card_management.shared.kafka.request.TransferSagaRequest;
import com.card_management.shared.redis.ResponseCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Listener
public class SagaListener {

    private final CardRepo repo;
    private final ResponseCacheService cacheService;
    private final TransactionResponseProducer producer;

    private static final String SERVICE_NAME = "card";

    @Autowired
    public SagaListener(CardRepo repo,
                        ResponseCacheService cacheService,
                        TransactionResponseProducer producer) {
        this.repo = repo;
        this.cacheService = cacheService;
        this.producer = producer;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @KafkaListener(topics = "transfer-saga", groupId = "card")
    public void listen(TransferSagaRequest request) {
        var transaction = request.dto();

        TransactionSagaResponse response;

        if (cacheService.cached(SERVICE_NAME, transaction.id())) {
            response = cacheService.get(SERVICE_NAME, transaction.id());
        } else {
            var fromCard = repo.findById(transaction.from());

            var toCard = repo.findById(transaction.to());

            if (fromCard.isEmpty() || toCard.isEmpty()) {
                response = new TransactionSagaResponse(transaction.id(), Optional.of("Card you requested is not exist!"), SERVICE_NAME);
            } else if (fromCard.get().getCardValue() - request.dto().amount() < 0) {
                    response = new TransactionSagaResponse(transaction.id(), Optional.of("You do not have enough money!"), SERVICE_NAME);
            }
            else if (fromCard.equals(toCard)) {
                response = new TransactionSagaResponse(transaction.id(), Optional.of("You cannot transfer money to same card!"), SERVICE_NAME);
            }
            else if (!fromCard.get().getOwnerId().equals(transaction.initiator())) {
                response = new TransactionSagaResponse(transaction.id(), Optional.of("You do not have access to this card!"), SERVICE_NAME);
            }
            else {
                fromCard.get().setCardValue(fromCard.get().getCardValue() - transaction.amount());
                toCard.get().setCardValue(toCard.get().getCardValue() + transaction.amount());

                response = new TransactionSagaResponse(transaction.id(), Optional.empty(), SERVICE_NAME);

                repo.save(fromCard.get());
                repo.save(toCard.get());
            }

            cacheService.setCache(SERVICE_NAME, transaction.id(), response);
        }

        producer.send(response);
    }
}

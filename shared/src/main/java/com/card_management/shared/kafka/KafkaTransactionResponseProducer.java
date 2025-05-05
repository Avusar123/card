package com.card_management.shared.kafka;

import com.card_management.shared.kafka.event.TransactionSagaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaTransactionResponseProducer implements TransactionResponseProducer {

    private final KafkaTemplate<String, TransactionSagaResponse> kafkaTemplate;

    @Autowired
    public KafkaTransactionResponseProducer(KafkaTemplate<String, TransactionSagaResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(TransactionSagaResponse response) {
        kafkaTemplate.send("transaction-saga-response", response);
    }
}

package com.card_management.transaction.infrastructure;

import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.event.TransactionSagaEvent;
import com.card_management.transaction.application.usecase.TransactionSageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaTransactionProducer implements TransactionSageProducer {

    private final KafkaTemplate<String, TransactionSagaEvent> kafkaTemplate;

    @Autowired
    public KafkaTransactionProducer(KafkaTemplate<String, TransactionSagaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(TransactionSagaEvent event) {
        kafkaTemplate.send("transaction-saga", event);
    }
}

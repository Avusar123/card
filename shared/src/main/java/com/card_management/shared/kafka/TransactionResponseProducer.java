package com.card_management.shared.kafka;

import com.card_management.shared.kafka.event.TransactionSagaResponse;

public interface TransactionResponseProducer {
    void send(TransactionSagaResponse response);
}

package com.card_management.transaction.application.usecase;

import com.card_management.shared.kafka.event.TransactionSagaEvent;

public interface TransactionSageProducer {
    void send(TransactionSagaEvent event);
}

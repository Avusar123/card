package com.card_management.transaction.infrastructure.saga.producer;

import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.request.TransferSagaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class TransferRequestProducer {

    private final KafkaTemplate<String, TransferSagaRequest> kafkaTemplate;

    @Autowired
    public TransferRequestProducer(KafkaTemplate<String, TransferSagaRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TransferSagaRequest request) {
        kafkaTemplate.send("transfer-saga", request.dto().id().uuid().toString(), request);
    }
}

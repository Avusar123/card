package com.card_management.transaction.infrastructure.saga.producer;

import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.request.LimitsCheckSagaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class LimitsCheckRequestProducer {

    private final KafkaTemplate<String, LimitsCheckSagaRequest> kafkaTemplate;

    @Autowired
    public LimitsCheckRequestProducer(KafkaTemplate<String, LimitsCheckSagaRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(LimitsCheckSagaRequest request) {
        kafkaTemplate.send("limits-check-saga", request.dto().id().uuid().toString(), request);
    }
}

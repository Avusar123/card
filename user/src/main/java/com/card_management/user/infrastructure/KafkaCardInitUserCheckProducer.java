package com.card_management.user.infrastructure;

import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.event.UserCheckResultEvent;
import com.card_management.user.application.CardInitUserCheckProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaCardInitUserCheckProducer implements CardInitUserCheckProducer {

    private final KafkaTemplate<String, UserCheckResultEvent> kafkaTemplate;

    @Autowired
    public KafkaCardInitUserCheckProducer(KafkaTemplate<String, UserCheckResultEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(@Valid UserCheckResultEvent event) {
        kafkaTemplate.send("user-check-result", event);
    }
}

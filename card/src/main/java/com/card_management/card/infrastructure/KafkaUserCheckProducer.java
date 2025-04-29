package com.card_management.card.infrastructure;

import com.card_management.card.application.UserCheckProducer;
import com.card_management.shared.kafka.Producer;
import com.card_management.shared.kafka.event.InitCardCreationEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaUserCheckProducer implements UserCheckProducer {
    private final KafkaTemplate<String, InitCardCreationEvent> kafkaTemplate;

    @Autowired
    public KafkaUserCheckProducer(KafkaTemplate<String, InitCardCreationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(@Valid InitCardCreationEvent event) {
        kafkaTemplate.send("init-card-creation", event);
    }
}

package com.card_management.card.infrastructure;

import com.card_management.card.application.UserCheckProducer;
import com.card_management.shared.kafka.event.InitCardCreationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaUserCheckProducer implements UserCheckProducer {
    private final KafkaTemplate<String, InitCardCreationEvent> kafkaTemplate;

    @Autowired
    public KafkaUserCheckProducer(KafkaTemplate<String, InitCardCreationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(InitCardCreationEvent event) {
        kafkaTemplate.send("init-card-creation", event);
    }
}

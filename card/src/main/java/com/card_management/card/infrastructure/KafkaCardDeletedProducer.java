package com.card_management.card.infrastructure;

import com.card_management.card.application.CardDeletedProducer;
import com.card_management.shared.Producer;
import com.card_management.shared.kafka.event.CardListDeletedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaCardDeletedProducer implements CardDeletedProducer {
    private final KafkaTemplate<String, CardListDeletedEvent> kafkaTemplate;

    @Autowired
    public KafkaCardDeletedProducer(KafkaTemplate<String, CardListDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(@Valid CardListDeletedEvent event) {
        kafkaTemplate.send("cards-deleted", event);
    }
}

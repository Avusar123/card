package com.card_management.user.infrastructure;

import com.card_management.shared.Producer;
import com.card_management.shared.kafka.event.UserDeletedEvent;
import com.card_management.user.application.UserDeletedProducer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Producer
public class KafkaUserDeletedProducer implements UserDeletedProducer {

    private final KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    @Autowired
    public KafkaUserDeletedProducer(KafkaTemplate<String, UserDeletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(@Valid UserDeletedEvent event) {
        kafkaTemplate.send("user-deleted", event);
    }
}

package com.card_management.card.application;

import com.card_management.shared.kafka.event.InitCardCreationEvent;
import jakarta.validation.Valid;

public interface UserCheckProducer {
    void send(@Valid InitCardCreationEvent event);
}

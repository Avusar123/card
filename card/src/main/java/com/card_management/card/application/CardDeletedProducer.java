package com.card_management.card.application;

import com.card_management.shared.kafka.event.CardListDeletedEvent;
import jakarta.validation.Valid;

public interface CardDeletedProducer {
    void send(@Valid CardListDeletedEvent event);
}

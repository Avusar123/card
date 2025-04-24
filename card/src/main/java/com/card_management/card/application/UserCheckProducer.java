package com.card_management.card.application;

import com.card_management.shared.kafka.event.InitCardCreationEvent;

public interface UserCheckProducer {
    public void send(InitCardCreationEvent event);
}

package com.card_management.user.application;

import com.card_management.shared.kafka.event.UserCheckResultEvent;
import jakarta.validation.Valid;

public interface CardInitUserCheckProducer {
    void send(@Valid UserCheckResultEvent event);
}

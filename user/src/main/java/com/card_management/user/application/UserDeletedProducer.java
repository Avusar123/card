package com.card_management.user.application;

import com.card_management.shared.kafka.event.UserDeletedEvent;
import jakarta.validation.Valid;

public interface UserDeletedProducer {
    void send(@Valid UserDeletedEvent event);
}

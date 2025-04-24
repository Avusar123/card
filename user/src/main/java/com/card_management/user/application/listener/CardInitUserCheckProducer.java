package com.card_management.user.application.listener;

import com.card_management.shared.kafka.event.UserCheckResultEvent;

public interface CardInitUserCheckProducer {
    void send(UserCheckResultEvent event);
}

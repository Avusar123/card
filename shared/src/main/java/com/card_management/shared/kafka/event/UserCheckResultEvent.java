package com.card_management.shared.kafka.event;

import java.util.UUID;

public record UserCheckResultEvent(UUID cardId, UUID userId, boolean exists) {
}

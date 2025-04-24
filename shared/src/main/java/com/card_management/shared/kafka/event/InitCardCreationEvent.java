package com.card_management.shared.kafka.event;

import java.util.UUID;

public record InitCardCreationEvent(UUID cardId, UUID userId) {
}

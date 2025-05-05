package com.card_management.shared.kafka.event;

import com.card_management.shared.id.UserId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDeletedEvent(
        @NotNull UserId userId) {
}

package com.card_management.limit.application.usecase.command;

import com.card_management.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SetLimitCommand(
        @NotNull(message = "LimitId must not be null")
        LimitId limitId,
        @PositiveOrZero int amount) {
}

package com.card_management.limit.application.usecase.command;

import com.card_management.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;

public record DeleteLimitByIdCommand(
        @NotNull(message = "LimitId must not be null") LimitId limitId) {
}

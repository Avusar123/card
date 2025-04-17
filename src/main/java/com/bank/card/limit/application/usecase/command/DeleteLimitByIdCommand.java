package com.bank.card.limit.application.usecase.command;

import com.bank.card.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;

public record DeleteLimitByIdCommand(
        @NotNull(message = "LimitId must not be null") LimitId limitId) {
}

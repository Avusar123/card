package com.bank.card.limit.application.usecase.command;

import com.bank.card.shared.id.LimitId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotNull;

public record GetLimitByIdCommand(
        @NotNull(message = "LimitId must not be null") @JsonUnwrapped LimitId limitId) {
}

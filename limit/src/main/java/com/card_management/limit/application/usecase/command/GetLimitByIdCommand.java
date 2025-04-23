package com.card_management.limit.application.usecase.command;

import com.card_management.shared.id.LimitId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotNull;

public record GetLimitByIdCommand(
        @NotNull(message = "LimitId must not be null") @JsonUnwrapped LimitId limitId) {
}

package com.bank.card.limit.application.usecase.command;

import com.bank.card.shared.id.CardId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotNull;

public record GetLimitsByCardIdCommand(
        @NotNull(message = "CardId must not be null") @JsonUnwrapped CardId cardId) {
}

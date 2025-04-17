package com.bank.card.limit.application.usecase.command;

import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

public record DeleteLimitsByCardIdCommand(
        @NotNull(message = "CardId must not be null") CardId cardId) {
}

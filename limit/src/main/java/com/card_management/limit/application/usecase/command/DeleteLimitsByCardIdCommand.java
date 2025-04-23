package com.card_management.limit.application.usecase.command;

import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

public record DeleteLimitsByCardIdCommand(
        @NotNull(message = "CardId must not be null") CardId cardId) {
}

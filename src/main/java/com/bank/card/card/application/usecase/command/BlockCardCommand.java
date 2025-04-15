package com.bank.card.card.application.usecase.command;

import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

public record BlockCardCommand(@NotNull(message = "CardId must not be null!") CardId cardId) {
}

package com.card_management.card.application.usecase.command;

import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

public record GetCardByIdCommand(@NotNull(message = "CardId must not be null!") CardId id) {
}

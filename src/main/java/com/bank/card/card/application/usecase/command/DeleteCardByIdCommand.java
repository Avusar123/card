package com.bank.card.card.application.usecase.command;

import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeleteCardByIdCommand(@NotNull(message = "CardId must not be null!") CardId id) {
}

package com.card_management.transaction.application.usecase.command;

import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateTransactionCommand(@NotBlank CardId fromId,
                                       @NotBlank CardId toId,
                                       @Positive int amount) {
}

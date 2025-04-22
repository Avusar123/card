package com.bank.card.transaction.application.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateTransactionCommand(@NotBlank String fromNumber,
                                       @NotBlank String toNumber,
                                       @Positive int amount) {
}

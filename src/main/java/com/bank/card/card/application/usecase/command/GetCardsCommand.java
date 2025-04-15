package com.bank.card.card.application.usecase.command;

import jakarta.validation.constraints.PositiveOrZero;

public record GetCardsCommand(
        @PositiveOrZero int page,
        @PositiveOrZero int size) {
}

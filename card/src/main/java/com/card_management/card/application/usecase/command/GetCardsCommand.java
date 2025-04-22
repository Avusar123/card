package com.card_management.card.application.usecase.command;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetCardsCommand(
        @PositiveOrZero int page,
        @Positive int size) {
}

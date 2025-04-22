package com.card_management.card.application.usecase.command;

import com.card_management.shared.id.UserId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetCardsByUserIdCommand(@NotNull(message = "UserId must not be null!") UserId userId,
                                      @PositiveOrZero int page,
                                      @Positive int size) {
}

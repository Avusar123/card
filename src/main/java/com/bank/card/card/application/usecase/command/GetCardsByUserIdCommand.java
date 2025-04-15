package com.bank.card.card.application.usecase.command;

import com.bank.card.shared.id.UserId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GetCardsByUserIdCommand(@NotNull(message = "UserId must not be null!") UserId userId,
                                      @PositiveOrZero int page,
                                      @PositiveOrZero int size) {
}

package com.bank.card.transaction.application.usecase.command;

import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.UserId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetUserTransactionsByRangeCommand(@NotNull UserId userId,
                                                @NotNull TimeRange range,
                                                @PositiveOrZero int page,
                                                @Positive int pageSize) {
}

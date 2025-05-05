package com.card_management.transaction.application.usecase.command;

import com.card_management.shared.TimeRange;
import com.card_management.shared.id.UserId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetUserTransactionsByRangeCommand(@NotNull UserId userId,
                                                @NotNull TimeRange range,
                                                @PositiveOrZero int page,
                                                @Positive int pageSize) {
}

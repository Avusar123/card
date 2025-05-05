package com.card_management.transaction.application.usecase.command;

import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetCardTransactionsByRangeCommand(@NotNull CardId cardId,
                                                @NotNull TimeRange range,
                                                @NotNull OperationType type,
                                                @PositiveOrZero int page,
                                                @Positive int pageSize) {
}

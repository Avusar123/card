package com.bank.card.transaction.application.usecase.command;

import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record GetCardTransactionsByRangeCommand(@NotNull CardId cardId,
                                                @NotNull TimeRange range,
                                                boolean income,
                                                @PositiveOrZero int page,
                                                @Positive int pageSize) {
}

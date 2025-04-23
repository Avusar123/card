package com.card_management.limit.application.usecase.request;

import com.card_management.limit.application.usecase.command.SetLimitCommand;
import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record SetLimitRequest(@NotNull(message = "CardId must not be null!") UUID cardId,
                              @NotNull(message = "LimitRange must not be null!") TimeRange range,
                              @PositiveOrZero(message = "Amount must be positive or zero") int amount) {
    public SetLimitCommand toCommand() {
        return new SetLimitCommand(new LimitId(range, new CardId(cardId)), amount);
    }
}

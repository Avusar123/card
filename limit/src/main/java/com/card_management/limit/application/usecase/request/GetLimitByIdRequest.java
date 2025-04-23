package com.card_management.limit.application.usecase.request;

import com.card_management.limit.application.usecase.command.GetLimitByIdCommand;
import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetLimitByIdRequest(
        @NotNull(message = "CardId must not be null!") UUID cardId,
        @NotNull(message = "LimitRange must not be null!") TimeRange range) {
    public GetLimitByIdCommand toCommand() {
        return new GetLimitByIdCommand(
                new LimitId(range, new CardId(cardId))
        );
    }
}

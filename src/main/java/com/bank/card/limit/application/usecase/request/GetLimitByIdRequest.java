package com.bank.card.limit.application.usecase.request;

import com.bank.card.limit.application.usecase.command.GetLimitByIdCommand;
import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
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

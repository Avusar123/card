package com.bank.card.limit.application.usecase.request;

import com.bank.card.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteLimitByIdRequest(
        @NotNull(message = "CardId must not be null!") UUID cardId,
        @NotNull(message = "LimitRange must not be null!") TimeRange range) {
    public DeleteLimitByIdCommand toCommand() {
        return new DeleteLimitByIdCommand(
                new LimitId(
                        range,
                        new CardId(cardId)
                )
        );
    }
}

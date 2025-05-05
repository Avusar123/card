package com.card_management.limit.application.usecase.request;

import com.card_management.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
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

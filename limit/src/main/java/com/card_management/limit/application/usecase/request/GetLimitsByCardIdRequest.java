package com.card_management.limit.application.usecase.request;

import com.card_management.limit.application.usecase.command.GetLimitsByCardIdCommand;
import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetLimitsByCardIdRequest(@NotNull(message = "CardId must not be null!") UUID cardId) {
    public GetLimitsByCardIdCommand toCommand() {
        return new GetLimitsByCardIdCommand(
                new CardId(cardId)
        );
    }
}

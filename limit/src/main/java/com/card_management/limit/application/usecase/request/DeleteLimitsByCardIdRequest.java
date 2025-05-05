package com.card_management.limit.application.usecase.request;

import com.card_management.limit.application.usecase.command.DeleteLimitsByCardIdCommand;
import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteLimitsByCardIdRequest(@NotNull(message = "CardId must not be null!") UUID cardId) {
    public DeleteLimitsByCardIdCommand toCommand() {
        return new DeleteLimitsByCardIdCommand(
                new CardId(cardId)
        );
    }
}

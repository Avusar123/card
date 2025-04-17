package com.bank.card.limit.application.usecase.request;

import com.bank.card.limit.application.usecase.command.DeleteLimitsByCardIdCommand;
import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteLimitsByCardIdRequest(@NotNull(message = "CardId must not be null!") UUID cardId) {
    public DeleteLimitsByCardIdCommand toCommand() {
        return new DeleteLimitsByCardIdCommand(
                new CardId(cardId)
        );
    }
}

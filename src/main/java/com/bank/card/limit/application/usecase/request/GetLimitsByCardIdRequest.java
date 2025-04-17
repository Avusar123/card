package com.bank.card.limit.application.usecase.request;

import com.bank.card.limit.application.usecase.command.GetLimitsByCardIdCommand;
import com.bank.card.shared.id.CardId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetLimitsByCardIdRequest(@NotNull(message = "CardId must not be null!") UUID cardId) {
    public GetLimitsByCardIdCommand toCommand() {
        return new GetLimitsByCardIdCommand(
                new CardId(cardId)
        );
    }
}

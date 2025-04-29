package com.card_management.transaction.application.usecase.request;

import com.card_management.shared.id.CardId;
import com.card_management.transaction.application.usecase.command.CreateTransactionCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateTransactionRequest(@NotNull @JsonProperty("from_card") UUID fromCardId,
                                       @NotNull @JsonProperty("to_card") UUID toCardId,
                                       @Positive int amount) {
    public CreateTransactionCommand toCommand() {
        return new CreateTransactionCommand(
                new CardId(fromCardId),
                new CardId(toCardId),
                amount
        );
    }
}

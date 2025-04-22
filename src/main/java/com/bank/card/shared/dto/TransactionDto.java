package com.bank.card.shared.dto;

import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.TransactionId;
import com.bank.card.shared.id.UserId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record TransactionDto(@JsonUnwrapped TransactionId id,
                             @JsonUnwrapped(prefix = "user_") UserId initiator,
                             @JsonUnwrapped(prefix = "card_from_") CardId from,
                             @JsonUnwrapped(prefix = "card_to_") CardId to,
                             int amount) {
}

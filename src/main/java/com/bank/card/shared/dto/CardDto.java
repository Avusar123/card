package com.bank.card.shared.dto;

import com.bank.card.card.domain.CardStatus;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDate;

public record CardDto(@JsonUnwrapped CardId cardId,
                      String ownerName,
                      @JsonUnwrapped(prefix = "owner_") UserId ownerId,
                      String number,
                      CardStatus status,
                      Integer current,
                      LocalDate expires) {
}

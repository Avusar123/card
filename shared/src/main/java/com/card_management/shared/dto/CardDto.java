package com.card_management.shared.dto;

import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDate;

public record CardDto(@JsonUnwrapped CardId cardId,
                      @JsonUnwrapped(prefix = "owner_") UserId ownerId,
                      String number,
                      CardStatus status,
                      Integer current,
                      LocalDate expires) {
}

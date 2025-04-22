package com.card_management.card.application.exception;

import com.card_management.shared.dto.CardStatus;

public class CardStatusMismatch extends IllegalArgumentException {
    public CardStatusMismatch(CardStatus expected, CardStatus current) {
        super("Card must have status %s but it is %s".formatted(expected, current));
    }
}

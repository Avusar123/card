package com.bank.card.card.application.exception;

import com.bank.card.card.domain.CardStatus;

public class CardStatusMismatch extends IllegalArgumentException {
    public CardStatusMismatch(CardStatus expected, CardStatus current) {
        super("Card must have status %s but it is %s".formatted(expected, current));
    }
}

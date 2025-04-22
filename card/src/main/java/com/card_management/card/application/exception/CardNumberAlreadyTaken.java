package com.card_management.card.application.exception;

public class CardNumberAlreadyTaken extends IllegalArgumentException {
    public CardNumberAlreadyTaken(String number) {
        super("Card with number %s already exists".formatted(number));
    }
}

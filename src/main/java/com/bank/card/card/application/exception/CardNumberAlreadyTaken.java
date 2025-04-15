package com.bank.card.card.application.exception;

public class CardNumberAlreadyTaken extends IllegalArgumentException {
    public CardNumberAlreadyTaken(String number) {
        super("Card with number %s already exists".formatted(number));
    }
}

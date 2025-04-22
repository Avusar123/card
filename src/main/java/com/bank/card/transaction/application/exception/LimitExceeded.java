package com.bank.card.transaction.application.exception;

public class LimitExceeded extends IllegalArgumentException {
    public LimitExceeded(String message) {
        super(message);
    }
}

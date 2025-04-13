package com.bank.card.user.application.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String email) {
        super("Email %s already exists!".formatted(email));
    }
}

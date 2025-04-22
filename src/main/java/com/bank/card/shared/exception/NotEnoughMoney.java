package com.bank.card.shared.exception;

public class NotEnoughMoney extends IllegalArgumentException {
    public NotEnoughMoney(String number, int amount) {
        super("Card with number %s does not have %d amount money to transfer".formatted(number, amount));
    }
}

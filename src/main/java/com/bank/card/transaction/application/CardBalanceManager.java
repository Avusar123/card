package com.bank.card.transaction.application;

import com.bank.card.shared.id.CardId;

public interface CardBalanceManager {
    void add(int amount, CardId cardId);

    void subtract(int amount, CardId cardId);

    boolean canSubtract(int amount, CardId cardId);
}

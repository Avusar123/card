package com.bank.card.card.application;

import com.bank.card.card.application.exception.CardStatusMismatch;
import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.exception.NotEnoughMoney;
import com.bank.card.shared.id.CardId;
import com.bank.card.transaction.application.CardBalanceManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class DefaultCardBalanceManager implements CardBalanceManager {

    private final CardRepo repo;

    public DefaultCardBalanceManager(CardRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void add(int amount, CardId cardId) {
        var card = repo.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card not found!"));

        throwIfInvalidStatus(card);

        Assert.isTrue(amount > 0, "Amount must be positive!");

        var value = card.getCardValue();

        card.setCardValue(value + amount);

        repo.save(card);
    }

    @Override
    @Transactional
    public void subtract(int amount, CardId cardId) {
        var card = repo.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card not found!"));

        throwIfInvalidStatus(card);

        Assert.isTrue(amount > 0, "Amount must be positive!");

        var value = card.getCardValue();

        var newValue = value - amount;

        if (newValue < 0) {
            throw new NotEnoughMoney(card.getNumber().mask(), amount);
        }

        card.setCardValue(newValue);

        repo.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSubtract(int amount, CardId cardId) {
        var card = repo.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Card not found!"));

        return card.getCardValue() >= amount;
    }

    private void throwIfInvalidStatus(Card card) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardStatusMismatch(CardStatus.ACTIVE, card.getStatus());
        }
    }


}

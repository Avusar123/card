package com.bank.card.card.application;

import com.bank.card.shared.id.CardId;

public interface LimitCascade {
    void deleteAllByCardId(CardId cardId);
}

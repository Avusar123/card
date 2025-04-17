package com.bank.card.shared;

import com.bank.card.shared.id.CardId;

public interface LimitCascade {
    void deleteAllByCardId(CardId cardId);
}

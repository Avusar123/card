package com.card_management.card.application;

import com.card_management.shared.id.CardId;

public interface LimitCascade {
    void deleteAllByCardId(CardId cardId);
}

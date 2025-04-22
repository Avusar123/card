package com.card_management.card.infrastructure;

import com.card_management.card.application.LimitCascade;
import com.card_management.shared.id.CardId;
import org.springframework.stereotype.Service;

@Service
public class DefaultLimitCascade implements LimitCascade {
    @Override
    public void deleteAllByCardId(CardId cardId) {

    }
}

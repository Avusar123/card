package com.bank.card.shared;

import com.bank.card.shared.dto.CardDto;
import com.bank.card.shared.id.CardId;

public interface CardProvider {
    CardDto getById(CardId id);

    boolean exist(CardId id);
}

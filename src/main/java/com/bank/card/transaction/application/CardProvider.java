package com.bank.card.transaction.application;

import com.bank.card.shared.dto.CardDto;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;

public interface CardProvider {
    CardDto getById(CardId id);

    CardDto getByHash(String hash);

    UserId getOwnerByRaw(String rawNumber);

    CardId getIdFromRaw(String rawNumber);

    boolean exist(CardId id);
}

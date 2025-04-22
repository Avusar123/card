package com.bank.card.transaction.application;

import com.bank.card.shared.dto.LimitDto;
import com.bank.card.shared.id.CardId;

import java.util.Set;

public interface LimitProvider {
    Set<LimitDto> getAll(CardId cardId);
}

package com.bank.card.shared;

import com.bank.card.shared.dto.CardDto;
import com.bank.card.shared.id.UserId;

public interface CardProvider {
    CardDto getById(UserId id);
}

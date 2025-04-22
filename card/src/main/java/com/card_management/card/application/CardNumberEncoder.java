package com.card_management.card.application;

import com.card_management.card.domain.CardNumber;

public interface CardNumberEncoder {
    CardNumber encode(String number);

    String decode(String hashed);
}

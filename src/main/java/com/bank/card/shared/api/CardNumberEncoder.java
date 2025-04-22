package com.bank.card.shared.api;

import com.bank.card.card.domain.CardNumber;

public interface CardNumberEncoder {
    CardNumber encode(String number);

    String decode(String hashed);
}

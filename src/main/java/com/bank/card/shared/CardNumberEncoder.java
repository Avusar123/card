package com.bank.card.shared;

import com.bank.card.card.domain.CardNumber;

public interface CardNumberEncoder {
    CardNumber encode(String number);

    String decode(String hashed);
}

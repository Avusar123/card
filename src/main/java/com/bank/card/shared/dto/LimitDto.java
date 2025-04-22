package com.bank.card.shared.dto;

import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record LimitDto(
        @JsonUnwrapped LimitId id,
        int amount) {
    public static LimitDto maxLimit(CardId id) {
        return new LimitDto(
                new LimitId(TimeRange.DAY, id),
                Integer.MAX_VALUE
        );
    }
}

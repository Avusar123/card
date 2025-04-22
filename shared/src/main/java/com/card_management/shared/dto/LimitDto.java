package com.card_management.shared.dto;

import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
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

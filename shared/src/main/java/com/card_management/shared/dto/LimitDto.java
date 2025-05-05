package com.card_management.shared.dto;

import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record LimitDto(
        @JsonUnwrapped LimitId id,
        int amount) {
}

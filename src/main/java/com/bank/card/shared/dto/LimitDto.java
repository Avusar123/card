package com.bank.card.shared.dto;

import com.bank.card.shared.id.LimitId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record LimitDto(
        @JsonUnwrapped LimitId id,
        int amount) {
}

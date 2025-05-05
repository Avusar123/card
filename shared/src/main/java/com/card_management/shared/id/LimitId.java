package com.card_management.shared.id;

import com.card_management.shared.TimeRange;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record LimitId(
        @NotNull @Enumerated(EnumType.STRING) TimeRange range,
        @JsonUnwrapped(prefix = "card_")
        CardId cardId) {
}

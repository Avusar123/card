package com.bank.card.shared.id;

import com.bank.card.limit.domain.LimitRange;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record LimitId(
        @NotNull @Enumerated(EnumType.STRING) LimitRange range,
        @JsonUnwrapped(prefix = "card_")
        CardId cardId) {
}

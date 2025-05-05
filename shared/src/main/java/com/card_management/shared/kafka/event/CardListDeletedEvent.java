package com.card_management.shared.kafka.event;

import com.card_management.shared.id.CardId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CardListDeletedEvent(
        @NotNull @NotEmpty List<CardId> list) {
}

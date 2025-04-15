package com.bank.card.shared.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record CardId(@JsonProperty(value = "id") UUID uuid) {
    public CardId() {
        this(UUID.randomUUID());
    }

    public CardId {
        Assert.notNull(uuid, "id must not be null");
    }
}
package com.card_management.shared.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record TransactionId(@JsonProperty(value = "id") UUID uuid) {
    public TransactionId() {
        this(UUID.randomUUID());
    }

    public TransactionId {
        Assert.notNull(uuid, "id must not be null");
    }
}
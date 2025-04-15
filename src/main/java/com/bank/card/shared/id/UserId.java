package com.bank.card.shared.id;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record UserId(@JsonProperty(value = "id") UUID uuid) {
    public UserId() {
        this(UUID.randomUUID());
    }

    public UserId {
        Assert.notNull(uuid, "id must not be null");
    }
}

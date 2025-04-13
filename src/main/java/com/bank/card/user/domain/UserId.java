package com.bank.card.user.domain;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record UserId(UUID uuid) {
    public UserId() {
        this(UUID.randomUUID());
    }

    public UserId {
        Assert.notNull(uuid, "id must not be null");
    }
}

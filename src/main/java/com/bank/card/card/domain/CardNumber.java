package com.bank.card.card.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.Assert;

@Embeddable
public record CardNumber(@NotBlank String mask, @NotBlank String hashed) {
    public CardNumber {
        Assert.isTrue(mask != null && !mask.isBlank(), "Masked value must not be blank!");

        Assert.isTrue(hashed != null && !hashed.isBlank(), "Hashed value must not be blank!");
    }
}

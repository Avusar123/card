package com.bank.card.limit.domain;

import com.bank.card.shared.id.LimitId;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "card_limit")
public class Limit {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "cardId.uuid", column = @Column(name = "card_id")),
    })
    LimitId id;

    @PositiveOrZero
    int maxAmount;

    public Limit(LimitId id, int maxAmount) {
        this.id = id;
        this.maxAmount = maxAmount;
    }

    Limit() {}

    public LimitId getId() {
        return id;
    }

    public void setId(LimitId id) {
        this.id = id;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}

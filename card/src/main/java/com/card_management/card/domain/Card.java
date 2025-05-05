package com.card_management.card.domain;

import com.card_management.shared.dto.CardStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Card {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    CardId id;

    @NotNull
    @AttributeOverride(name = "uuid", column = @Column(name = "owner_id"))
    UserId ownerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "mask", column = @Column(name = "masked_number")),
            @AttributeOverride(name = "hashed", column = @Column(name = "hashed_number", unique = true))
    })
    CardNumber number;

    @Enumerated(EnumType.STRING)
    CardStatus status;

    int cardValue;

    LocalDate expires;

    public Card(CardId id, UserId ownerId, CardNumber number, CardStatus status, int value, LocalDate expires) {
        this.id = id;
        this.ownerId = ownerId;
        this.number = number;
        this.status = status;
        this.expires = expires;
        this.cardValue = value;
    }

    public Card() {
    }

    @PostLoad
    public void checkExpiration() {
        if (expires != null && expires.isBefore(LocalDate.now())) {
            this.status = CardStatus.EXPIRED;
        }
    }

    public CardId getId() {
        return id;
    }

    public void setId(CardId id) {
        this.id = id;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserId ownerId) {
        this.ownerId = ownerId;
    }

    public CardNumber getNumber() {
        return number;
    }

    public void setNumber(CardNumber number) {
        this.number = number;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public LocalDate getExpires() {
        return expires;
    }

    public void setExpires(LocalDate expires) {
        this.expires = expires;
    }
}

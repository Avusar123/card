package com.bank.card.card.domain;

import com.bank.card.shared.id.CardId;
import com.bank.card.user.domain.UserModel;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    CardId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserModel owner;

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

    public Card(CardId id, UserModel owner, CardNumber number, CardStatus status, int value, LocalDate expires) {
        this.id = id;
        this.owner = owner;
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

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
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

    public LocalDate getExpires() {
        return expires;
    }

    public void setExpires(LocalDate expires) {
        this.expires = expires;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}

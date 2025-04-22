package com.bank.card.transaction.domain;

import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.TransactionId;
import com.bank.card.shared.id.UserId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_model")
public class Transaction {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    TransactionId id;

    @Embedded
    @AttributeOverride(name = "uuid", column = @Column(name = "fromId"))
    CardId fromId;

    @Embedded
    @AttributeOverride(name = "uuid", column = @Column(name = "toId"))
    CardId toId;

    @Embedded
    @AttributeOverride(name = "uuid", column = @Column(name = "initiatorId"))
    UserId initiator;

    int amount;

    LocalDateTime time;

    public Transaction(TransactionId id,
                       CardId fromId,
                       CardId toId,
                       UserId initiator,
                       LocalDateTime time,
                       int amount) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.initiator = initiator;
        this.time = time;
        this.amount = amount;
    }

    Transaction() {
    }

    public TransactionId getId() {
        return id;
    }

    public void setId(TransactionId id) {
        this.id = id;
    }

    public CardId getFromId() {
        return fromId;
    }

    public void setFromId(CardId fromId) {
        this.fromId = fromId;
    }

    public CardId getToId() {
        return toId;
    }

    public void setToId(CardId toId) {
        this.toId = toId;
    }

    public UserId getInitiator() {
        return initiator;
    }

    public void setInitiator(UserId initiator) {
        this.initiator = initiator;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

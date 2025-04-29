package com.card_management.transaction.domain;

import com.card_management.shared.dto.TransactionStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.TransactionId;
import com.card_management.shared.id.UserId;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Column(name = "failure")
    String failureReason;

    int amount;

    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    LocalDateTime createdTime;

    LocalDateTime completedTime;

    public Transaction(TransactionId id,
                       CardId fromId,
                       CardId toId,
                       UserId initiator,
                       LocalDateTime createdTime,
                       int amount) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.initiator = initiator;
        this.createdTime = createdTime;
        this.status = TransactionStatus.PROCESSING;
        this.amount = amount;
        this.failureReason = null;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Optional<String> getFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }
}

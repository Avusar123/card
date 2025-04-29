package com.card_management.shared.kafka.event;

import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;

import java.util.Map;
import java.util.UUID;

public class TransactionSagaEvent extends SagaEvent {

    private int amount;

    private CardId fromId;

    private CardId toId;

    private UserId userId;

    private Map<TimeRange, Integer> transactionSums;

    private String failureReason;

    public TransactionSagaEvent(UUID sagaId,
                                String eventType,
                                String sourceName,
                                int amount,
                                CardId fromId,
                                CardId toId,
                                UserId userId,
                                Map<TimeRange, Integer> transactionSums) {
        super(sagaId, eventType, sourceName);
        this.amount = amount;
        this.fromId = fromId;
        this.toId = toId;
        this.userId = userId;
        this.transactionSums = transactionSums;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Map<TimeRange, Integer> getTransactionSums() {
        return transactionSums;
    }

    public void setTransactionSums(Map<TimeRange, Integer> transactionSums) {
        this.transactionSums = transactionSums;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}

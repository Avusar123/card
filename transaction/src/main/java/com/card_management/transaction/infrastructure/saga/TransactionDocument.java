package com.card_management.transaction.infrastructure.saga;

import com.card_management.shared.dto.TransactionMiniDto;
import com.card_management.shared.id.TransactionId;
import jakarta.persistence.EmbeddedId;

import java.time.LocalDateTime;
import java.util.Optional;

public class TransactionDocument {

    @EmbeddedId
    TransactionId id;

    TransactionMiniDto miniDto;

    TransactionSagaStep sagaStep;

    LocalDateTime lastAttemptTime;

    String failureReason;

    public TransactionDocument(TransactionMiniDto dto) {
        this.miniDto = dto;
        lastAttemptTime = LocalDateTime.now();
        this.id = dto.id();
    }

    TransactionDocument() {

    }

    public TransactionId getId() {
        return id;
    }

    public TransactionSagaStep getSagaStep() {
        return sagaStep;
    }

    public void setSagaStep(TransactionSagaStep sagaStep) {
        this.sagaStep = sagaStep;
    }

    public LocalDateTime getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(LocalDateTime lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public Optional<String> getFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public TransactionMiniDto getMiniDto() {
        return miniDto;
    }
}

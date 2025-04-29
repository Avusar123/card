package com.card_management.shared.kafka.event;

import java.util.UUID;

public abstract class SagaEvent {
    private UUID sagaId;

    private String eventType;

    private String sourceName;

    private String rollbackReason;

    public SagaEvent(UUID sagaId, String eventType, String sourceName) {
        this.sagaId = sagaId;
        this.eventType = eventType;
        this.sourceName = sourceName;
    }

    public UUID getSagaId() {
        return sagaId;
    }

    public void setSagaId(UUID sagaId) {
        this.sagaId = sagaId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getRollbackReason() {
        return rollbackReason;
    }

    public boolean hasRollbackReason() {
        return rollbackReason != null;
    }

    public void setRollbackReason(String rollbackReason) {
        this.rollbackReason = rollbackReason;
    }
}

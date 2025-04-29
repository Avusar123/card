package com.card_management.shared.kafka.saga;

public enum SagaEventStage {
    PROCESS,
    ROLLBACK,
    MISS
}

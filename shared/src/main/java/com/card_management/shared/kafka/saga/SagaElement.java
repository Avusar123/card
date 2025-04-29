package com.card_management.shared.kafka.saga;

import com.card_management.shared.kafka.event.SagaEvent;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class SagaElement<T extends SagaEvent> {
    protected KafkaTemplate<String, T> kafkaTemplate;

    protected SagaElement(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    protected abstract String name();

    protected abstract String okEventType();

    protected abstract String rollbackEventType();

    protected abstract String topicName();

    protected SagaEventStage getStage(T event) {
        var eventType = event.getEventType();

        if (eventType.equals(okEventType())) {
            return SagaEventStage.PROCESS;
        }

        if (eventType.equals(rollbackEventType())) {
            return SagaEventStage.ROLLBACK;
        }

        return SagaEventStage.MISS;
    }

    protected void initRollback(T event) {
        event.setSourceName(name());

        kafkaTemplate.send(topicName(), event);
    }

    protected void processNext(T event) {
        event.setSourceName(name());

        kafkaTemplate.send(topicName(), event);
    }

    protected boolean shouldProcess(T event) {
        return getStage(event) != SagaEventStage.MISS;
    }
}

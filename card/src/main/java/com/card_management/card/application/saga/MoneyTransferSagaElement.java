package com.card_management.card.application.saga;

import com.card_management.shared.kafka.event.TransactionSagaEvent;
import com.card_management.shared.kafka.saga.SagaElement;
import com.card_management.shared.kafka.saga.SagaEventStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

public class MoneyTransferSagaElement extends SagaElement<TransactionSagaEvent> {

    @Autowired
    public MoneyTransferSagaElement(KafkaTemplate<String, TransactionSagaEvent> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected String name() {
        return "card-service";
    }

    @Override
    protected String okEventType() {
        return "limit-checked";
    }

    @Override
    protected String rollbackEventType() {
        return "";
    }

    @Override
    protected String topicName() {
        return "transaction-created";
    }

    @KafkaListener(topics = "transaction-created", groupId = "card")
    public void listen(TransactionSagaEvent event) {
        var stage = getStage(event);

        if (stage == SagaEventStage.PROCESS) {
        }
    }
}

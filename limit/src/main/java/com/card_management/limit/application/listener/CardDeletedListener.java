package com.card_management.limit.application.listener;

import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.kafka.Listener;
import com.card_management.shared.kafka.event.CardListDeletedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Transactional;

@Listener
public class CardDeletedListener {

    private final LimitRepo repo;

    @Autowired
    public CardDeletedListener(LimitRepo repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "cards-deleted", groupId = "limit")
    @Transactional
    public void listen(@Valid CardListDeletedEvent event, Acknowledgment ack) {
        for (var cardId : event.list()) {
            repo.deleteAllByCardId(cardId);
        }

        ack.acknowledge();
    }
}

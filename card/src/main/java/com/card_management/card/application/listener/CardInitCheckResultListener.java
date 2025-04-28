package com.card_management.card.application.listener;

import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.Listener;
import com.card_management.shared.dto.CardStatus;
import com.card_management.shared.kafka.event.UserCheckResultEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Listener
public class CardInitCheckResultListener {

    private final CardRepo repo;

    @Autowired
    public CardInitCheckResultListener(CardRepo repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "user-check-result", groupId = "card")
    @Transactional
    public void listen(@Valid UserCheckResultEvent event, Acknowledgment ack) {
        var id = event.cardId();

        var card = repo.findById(id);

        if (event.exists() && card.isPresent()) {
            var cardEntity = card.get();

            cardEntity.setStatus(CardStatus.ACTIVE);

            repo.save(cardEntity);
        } else {
            repo.deleteById(id);
        }

        ack.acknowledge();
    }
}

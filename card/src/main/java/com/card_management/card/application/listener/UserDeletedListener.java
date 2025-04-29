package com.card_management.card.application.listener;

import com.card_management.card.application.CardDeletedProducer;
import com.card_management.card.domain.Card;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.kafka.Listener;
import com.card_management.shared.kafka.event.CardListDeletedEvent;
import com.card_management.shared.kafka.event.UserDeletedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Transactional;

@Listener
public class UserDeletedListener {
    private final CardRepo repo;
    private final CardDeletedProducer producer;

    @Autowired
    public UserDeletedListener(CardRepo repo, CardDeletedProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @KafkaListener(topics = "user-deleted", groupId = "card")
    @Transactional
    public void listen(@Valid UserDeletedEvent event, Acknowledgment ack) {
        var id = event.userId();

        var cards = repo.findAllByOwnerId(id, Pageable.unpaged());

        repo.deleteAll(cards);

        producer.send(new CardListDeletedEvent(cards.stream().map(Card::getId).toList()));

        ack.acknowledge();
    }
}

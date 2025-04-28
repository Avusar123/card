package com.card_management.user.application.listener;

import com.card_management.shared.Listener;
import com.card_management.shared.id.UserId;
import com.card_management.shared.kafka.event.InitCardCreationEvent;
import com.card_management.shared.kafka.event.UserCheckResultEvent;
import com.card_management.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Listener
public class CardInitUserCheckListener {

    private final UserRepo userRepo;
    private final CardInitUserCheckProducer producer;

    public CardInitUserCheckListener(UserRepo userRepo,
                                     CardInitUserCheckProducer producer) {
        this.userRepo = userRepo;
        this.producer = producer;
    }

    @KafkaListener(topics = "init-card-creation", groupId = "user-group")
    @Transactional(readOnly = true)
    public void listen(@Valid InitCardCreationEvent event, Acknowledgment ack) {
        var userOpt = userRepo.findById(event.userId());

        producer.send(new UserCheckResultEvent(event.cardId(), event.userId(), userOpt.isPresent()));

        ack.acknowledge();
    }
}

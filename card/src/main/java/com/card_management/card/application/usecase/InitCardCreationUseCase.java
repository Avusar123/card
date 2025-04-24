package com.card_management.card.application.usecase;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.application.UserCheckProducer;
import com.card_management.card.application.exception.CardNumberAlreadyTaken;
import com.card_management.card.application.usecase.command.CreateCardCommand;
import com.card_management.card.domain.Card;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.shared.UseCase;
import com.card_management.shared.dto.CardDto;
import com.card_management.shared.dto.CardStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import com.card_management.shared.kafka.event.InitCardCreationEvent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@UseCase
public class InitCardCreationUseCase {

    private final CardNumberEncoder cardNumberGenerator;
    private final CardRepo cardRepo;
    private final UserCheckProducer userCheckProducer;

    @Autowired
    public InitCardCreationUseCase(CardNumberEncoder cardNumberGenerator,
                                   CardRepo cardRepo,
                                   UserCheckProducer userCheckProducer) {
        this.cardNumberGenerator = cardNumberGenerator;
        this.cardRepo = cardRepo;
        this.userCheckProducer = userCheckProducer;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public CardDto execute(@NotNull @Valid CreateCardCommand command) {
        if (command.expires() == null || command.expires().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expire date must be after this day!");
        }

        if (command.cardNumber() == null || !command.cardNumber().matches("\\d+")) {
            throw new IllegalArgumentException("Card number must only contain numbers!");
        }

        if (command.value() < 0) {
            throw new IllegalArgumentException("Value must be positive!");
        }

        var userId = new UserId(command.userId());

        var number = cardNumberGenerator.encode(command.cardNumber());

        if (cardRepo.existsByHashedNumber(number.hashed())) {
            throw new CardNumberAlreadyTaken(command.cardNumber());
        }

        var card = new Card(new CardId(),
                userId,
                number,
                CardStatus.CREATING,
                command.value(),
                command.expires());


        card = cardRepo.save(card);

        userCheckProducer.send(new InitCardCreationEvent(card.getId().uuid(), card.getOwnerId().uuid() ));

        return new CardDto(
                card.getId(),
                card.getOwnerId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        );
    }
}

package com.bank.card.card.application.usecase;

import com.bank.card.card.application.exception.CardNumberAlreadyTaken;
import com.bank.card.card.application.usecase.command.CreateCardCommand;
import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.CardNumberEncoder;
import com.bank.card.shared.UseCase;
import com.bank.card.shared.UserProvider;
import com.bank.card.shared.dto.CardDto;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@UseCase
public class CreateCardUseCase {

    private final CardNumberEncoder cardNumberGenerator;
    private final CardRepo cardRepo;
    private final UserProvider userProvider;

    @Autowired
    public CreateCardUseCase(CardNumberEncoder cardNumberGenerator,
                             CardRepo cardRepo,
                             UserProvider userProvider) {
        this.cardNumberGenerator = cardNumberGenerator;
        this.cardRepo = cardRepo;
        this.userProvider = userProvider;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public CardDto execute(@Valid CreateCardCommand command) {
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

        if (!userProvider.exist(userId)) {
            throw new EntityNotFoundException("This user not exist!");
        }

        var number = cardNumberGenerator.encode(command.cardNumber());

        if (cardRepo.existsByHashedNumber(number.hashed())) {
            throw new CardNumberAlreadyTaken(command.cardNumber());
        }

        var card = new Card(new CardId(),
                new UserModel(userId),
                number,
                CardStatus.ACTIVE,
                command.value(),
                command.expires());


        card = cardRepo.save(card);

        return new CardDto(
                card.getId(),
                card.getOwner().getName(),
                card.getOwner().getId(),
                card.getNumber().mask(),
                card.getStatus(),
                card.getCardValue(),
                card.getExpires()
        );
    }
}

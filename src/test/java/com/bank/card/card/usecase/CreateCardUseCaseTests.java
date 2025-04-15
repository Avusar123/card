package com.bank.card.card.usecase;

import com.bank.card.card.application.exception.CardNumberAlreadyTaken;
import com.bank.card.card.application.usecase.CreateCardUseCase;
import com.bank.card.card.application.usecase.command.CreateCardCommand;
import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardNumber;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.CardNumberEncoder;
import com.bank.card.shared.UserProvider;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserModel;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateCardUseCaseTests {
    @InjectMocks
    CreateCardUseCase createCardUseCase;

    @Mock
    CardNumberEncoder cardNumberGenerator;

    @Mock
    CardRepo cardRepo;

    @Mock
    UserProvider userProvider;

    @Test
    public void createCard_exception_userNotExist() {
        var createCardCommand = new CreateCardCommand(
                UUID.randomUUID(),
                "0".repeat(16),
                0,
                LocalDate.now().plusDays(1)
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> createCardUseCase.execute(createCardCommand));
    }

    @Test
    public void executeCard_exception_cardNumberAlreadyTaken() {
        var number = "0".repeat(16);

        var userId = new UserId();

        var createCardCommand = new CreateCardCommand(
                userId.uuid(),
                number,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(cardNumberGenerator.encode(number)).thenReturn(new CardNumber("Mask", "Hashed"));

        Mockito.when(cardRepo.existsByHashedNumber("Hashed")).thenReturn(true);

        Mockito.when(userProvider.exist(userId)).thenReturn(true);

        Assertions.assertThrows(CardNumberAlreadyTaken.class, () -> createCardUseCase.execute(createCardCommand));
    }

    @Test
    public void executeCard_exception_dateInPast() {
        var number = "0".repeat(16);

        var userId = new UserId();

        var createCardCommand = new CreateCardCommand(
                userId.uuid(),
                number,
                0,
                LocalDate.now().minusDays(1)
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> createCardUseCase.execute(createCardCommand));
    }

    @Test
    public void createCard_success_valid() {
        var number = "0".repeat(16);

        var userId = new UserId();

        var expires = LocalDate.now().plusDays(1);

        var owner = new UserModel(userId);

        var cardNumber = new CardNumber("Mask", "Hashed");

        owner.setName("Name");

        var createCardCommand = new CreateCardCommand(
                userId.uuid(),
                number,
                0,
                expires
        );

        var card = new Card(new CardId(),
                owner,
                cardNumber,
                CardStatus.ACTIVE,
                0,
                expires);


        Mockito.when(cardNumberGenerator.encode(number)).thenReturn(cardNumber);

        Mockito.when(cardRepo.save(Mockito.any())).thenReturn(card);

        Mockito.when(userProvider.exist(userId)).thenReturn(true);

        var result = createCardUseCase.execute(createCardCommand);

        Assertions.assertEquals("Mask", result.number());

        Assertions.assertEquals(userId, result.ownerId());
    }
}

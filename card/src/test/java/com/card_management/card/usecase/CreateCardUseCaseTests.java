package com.card_management.card.usecase;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.application.exception.CardNumberAlreadyTaken;
import com.card_management.card.application.usecase.InitCardCreationUseCase;
import com.card_management.card.application.usecase.command.CreateCardCommand;
import com.card_management.card.domain.Card;
import com.card_management.card.domain.CardNumber;
import com.card_management.card.infrastructure.CardRepo;
import com.card_management.card.infrastructure.KafkaUserCheckProducer;
import com.card_management.shared.dto.CardStatus;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CreateCardUseCaseTests {
    @InjectMocks
    InitCardCreationUseCase initCardCreationUseCase;

    @Mock
    CardNumberEncoder cardNumberGenerator;

    @Mock
    KafkaUserCheckProducer producer;

    @Mock
    CardRepo cardRepo;

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

        Assertions.assertThrows(CardNumberAlreadyTaken.class, () -> initCardCreationUseCase.execute(createCardCommand));
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

        Assertions.assertThrows(IllegalArgumentException.class, () -> initCardCreationUseCase.execute(createCardCommand));
    }

    @Test
    public void createCard_success_valid() {
        var number = "0".repeat(16);

        var userId = new UserId();

        var expires = LocalDate.now().plusDays(1);

        var cardNumber = new CardNumber("Mask", "Hashed");


        var createCardCommand = new CreateCardCommand(
                userId.uuid(),
                number,
                0,
                expires
        );

        var card = new Card(new CardId(),
                userId,
                cardNumber,
                CardStatus.CREATING,
                0,
                expires);


        Mockito.when(cardNumberGenerator.encode(number)).thenReturn(cardNumber);

        Mockito.when(cardRepo.save(Mockito.any())).thenReturn(card);

        var result = initCardCreationUseCase.execute(createCardCommand);

        Assertions.assertEquals("Mask", result.number());

        Assertions.assertEquals(userId, result.ownerId());
    }
}

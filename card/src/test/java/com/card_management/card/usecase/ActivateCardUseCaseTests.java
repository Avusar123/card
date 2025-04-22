package com.card_management.card.usecase;

import com.card_management.card.application.exception.CardStatusMismatch;
import com.card_management.card.application.usecase.ActivateCardUseCase;
import com.card_management.card.application.usecase.command.ActivateCardCommand;
import com.card_management.card.domain.Card;
import com.card_management.card.domain.CardNumber;
import com.card_management.card.infrastructure.CardRepo;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ActivateCardUseCaseTests {
    @Mock
    CardRepo repo;
    @InjectMocks
    private ActivateCardUseCase activateCardUseCase;

    @Test
    void activateCard_success_activated() {
        var id = new CardId();

        var card = new Card(
                id,
                new UserId(),
                new CardNumber("Mask", "Hashed"),
                CardStatus.BLOCKED,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(card));

        activateCardUseCase.execute(new ActivateCardCommand(id));

        Assertions.assertEquals(CardStatus.ACTIVE, card.getStatus());
    }

    @Test
    void activateCard_exception_statusMismatch() {
        var id = new CardId();

        var card = new Card(
                id,
                new UserId(),
                new CardNumber("Mask", "Hashed"),
                CardStatus.EXPIRED,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(card));

        Assertions.assertThrows(CardStatusMismatch.class, () -> activateCardUseCase.execute(new ActivateCardCommand(id)));
    }
}

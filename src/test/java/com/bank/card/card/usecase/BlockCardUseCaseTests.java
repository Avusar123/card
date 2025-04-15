package com.bank.card.card.usecase;

import com.bank.card.card.application.exception.CardStatusMismatch;
import com.bank.card.card.application.usecase.BlockCardUseCase;
import com.bank.card.card.application.usecase.command.BlockCardCommand;
import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardNumber;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.id.CardId;
import com.bank.card.user.domain.UserModel;
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
public class BlockCardUseCaseTests {
    @Mock
    CardRepo repo;
    @InjectMocks
    private BlockCardUseCase blockCardUseCase;

    @Test
    void blockCard_success_blocked() {
        var id = new CardId();

        var card = new Card(
                id,
                new UserModel(),
                new CardNumber("Mask", "Hashed"),
                CardStatus.ACTIVE,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(card));

        blockCardUseCase.execute(new BlockCardCommand(id));

        Assertions.assertEquals(CardStatus.BLOCKED, card.getStatus());
    }

    @Test
    void blockCard_exception_statusMismatch() {
        var id = new CardId();

        var card = new Card(
                id,
                new UserModel(),
                new CardNumber("Mask", "Hashed"),
                CardStatus.EXPIRED,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(card));

        Assertions.assertThrows(CardStatusMismatch.class, () -> blockCardUseCase.execute(new BlockCardCommand(id)));
    }
}

package com.bank.card.card.usecase;

import com.bank.card.card.application.usecase.GetCardsUseCase;
import com.bank.card.card.application.usecase.command.GetCardsCommand;
import com.bank.card.card.domain.Card;
import com.bank.card.card.domain.CardNumber;
import com.bank.card.card.domain.CardStatus;
import com.bank.card.card.infrastructure.CardRepo;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.domain.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetAllCardsUseCaseTests {
    @InjectMocks
    GetCardsUseCase getCardsUseCase;

    @Mock
    CardRepo cardRepo;

    @Test
    public void createCard_success() {
        var cardId = new CardId();

        var card = new Card(
                cardId,
                new UserModel(
                        new UserId(),
                        UserRole.CLIENT,
                        "test@example.com",
                        "Hash",
                        "Name"),
                new CardNumber(
                        "Mask",
                        "Hashed"
                ),
                CardStatus.ACTIVE,
                0,
                LocalDate.now().plusDays(1)
        );

        Mockito.when(cardRepo.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(card)));

        var result = getCardsUseCase.execute(new GetCardsCommand(0, 1));

        Assertions.assertEquals(cardId, result.getFirst().cardId());
    }
}

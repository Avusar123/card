package com.card_management.card.usecase;

import com.card_management.card.application.usecase.GetCardsUseCase;
import com.card_management.card.application.usecase.command.GetCardsCommand;
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
                new UserId(),
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

        Assertions.assertEquals(cardId, result.get(0).cardId());
    }
}

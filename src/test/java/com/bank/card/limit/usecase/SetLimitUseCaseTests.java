package com.bank.card.limit.usecase;

import com.bank.card.limit.application.usecase.SetLimitUseCase;
import com.bank.card.limit.application.usecase.command.SetLimitCommand;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.TimeRange;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
import com.bank.card.transaction.application.CardProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SetLimitUseCaseTests {
    @InjectMocks
    SetLimitUseCase setLimitUseCase;

    @Mock
    CardProvider cardProvider;

    @Mock
    LimitRepo limitRepo;

    @Test
    public void setLimit_exception_cardNotExist() {
        var limitId = new LimitId(
                TimeRange.DAY,
                new CardId()
        );

        var command = new SetLimitCommand(
                limitId,
                0
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> setLimitUseCase.execute(command));
    }

    @Test
    public void setLimit_success_new() {
        var cardId = new CardId();

        var limitId = new LimitId(
                TimeRange.DAY,
                cardId
        );

        var command = new SetLimitCommand(
                limitId,
                0
        );

        Mockito.when(cardProvider.exist(cardId)).thenReturn(true);

        var result = setLimitUseCase.execute(command);

        Assertions.assertEquals(cardId, result.id().cardId());

        Assertions.assertEquals(TimeRange.DAY, result.id().range());

        Assertions.assertEquals(0, result.amount());
    }
}

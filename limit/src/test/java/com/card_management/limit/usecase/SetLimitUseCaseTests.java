package com.card_management.limit.usecase;

import com.card_management.limit.application.usecase.SetLimitUseCase;
import com.card_management.limit.application.usecase.command.SetLimitCommand;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SetLimitUseCaseTests {
    @InjectMocks
    SetLimitUseCase setLimitUseCase;

    @Mock
    LimitRepo limitRepo;

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

        var result = setLimitUseCase.execute(command);

        Assertions.assertEquals(cardId, result.id().cardId());

        Assertions.assertEquals(TimeRange.DAY, result.id().range());

        Assertions.assertEquals(0, result.amount());
    }
}

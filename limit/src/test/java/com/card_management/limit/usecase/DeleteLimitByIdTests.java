package com.card_management.limit.usecase;

import com.card_management.limit.application.usecase.DeleteLimitByIdUseCase;
import com.card_management.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.card_management.limit.infrastructure.LimitRepo;
import com.card_management.shared.TimeRange;
import com.card_management.shared.id.CardId;
import com.card_management.shared.id.LimitId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteLimitByIdTests {

    @Mock
    LimitRepo repo;

    @InjectMocks
    private DeleteLimitByIdUseCase deleteLimitByIdUseCase;

    @Test
    void getUser_success_found() {

        var command = new DeleteLimitByIdCommand(
                new LimitId(TimeRange.DAY, new CardId())
        );

        deleteLimitByIdUseCase.execute(command);
    }

}

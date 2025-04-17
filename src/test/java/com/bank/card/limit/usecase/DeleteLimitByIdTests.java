package com.bank.card.limit.usecase;

import com.bank.card.limit.application.usecase.DeleteLimitByIdUseCase;
import com.bank.card.limit.application.usecase.command.DeleteLimitByIdCommand;
import com.bank.card.limit.domain.LimitRange;
import com.bank.card.limit.infrastructure.LimitRepo;
import com.bank.card.shared.id.CardId;
import com.bank.card.shared.id.LimitId;
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
                new LimitId(LimitRange.DAY, new CardId())
        );

        deleteLimitByIdUseCase.execute(command);
    }

}

package com.bank.card.user.usecase;

import com.bank.card.user.application.usecase.GetUsersUseCase;
import com.bank.card.user.application.usecase.command.GetUsersCommand;
import com.bank.card.user.domain.UserId;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.domain.UserRole;
import com.bank.card.user.infrastructure.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetUsersUseCaseTests {

    @InjectMocks
    private GetUsersUseCase getUsersUseCase;

    @Mock
    UserRepo repo;

    @Test
    void getUser_success_list() {
        var email = "test@gmail.com";

        var command = new GetUsersCommand(0, 3);

        var user = new UserModel(
                    new UserId(),
                    UserRole.CLIENT,
                    email,
                    "Encoded",
                    "Test");

        Mockito.when(repo.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(user)));

        var result = getUsersUseCase.execute(command);

        Assertions.assertFalse(result.isEmpty());
    }

}

package com.bank.card.user.usecase;

import com.bank.card.user.application.usecase.GetUserByEmailUseCase;
import com.bank.card.user.application.usecase.command.GetUserByEmailCommand;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetUserByEmailUseCaseTests {

    @InjectMocks
    private GetUserByEmailUseCase getUserByEmailUseCase;

    @Mock
    UserRepo repo;

    @Test
    void getUser_exception_notFound() {
        var command = new GetUserByEmailCommand(
                "test@gmail.com"
        );

        Assertions.assertThrows(UsernameNotFoundException.class, () -> getUserByEmailUseCase.execute(command));
    }

    @Test
    void getUser_success_found() {
        var email = "test@gmail.com";

        var command = new GetUserByEmailCommand(
                email
        );

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(new UserModel(
                new UserId(),
                UserRole.CLIENT,
                email,
                "Encoded",
                "Test"
        )));

        var result = getUserByEmailUseCase.execute(command);

        Assertions.assertEquals(email, result.email());
    }

}

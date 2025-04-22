package com.card_management.user.usecase;

import com.card_management.shared.id.UserId;
import com.card_management.shared.dto.UserRole;
import com.card_management.user.application.usecase.GetUserByEmailUseCase;
import com.card_management.user.application.usecase.command.GetUserByEmailCommand;
import com.card_management.user.domain.UserModel;
import com.card_management.user.infrastructure.UserRepo;
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

    @Mock
    UserRepo repo;
    @InjectMocks
    private GetUserByEmailUseCase getUserByEmailUseCase;

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

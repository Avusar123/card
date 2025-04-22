package com.card_management.user.usecase;

import com.card_management.shared.id.UserId;
import com.card_management.user.application.usecase.DeleteUserByEmailUseCase;
import com.card_management.user.application.usecase.command.DeleteUserByEmailCommand;
import com.card_management.user.domain.UserModel;
import com.card_management.shared.dto.UserRole;
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
public class DeleteUserByEmailTests {

    @Mock
    UserRepo repo;
    @InjectMocks
    private DeleteUserByEmailUseCase deleteUserByEmailUseCase;

    @Test
    void getUser_exception_notFound() {
        var command = new DeleteUserByEmailCommand(
                "test@gmail.com"
        );

        Assertions.assertThrows(UsernameNotFoundException.class, () -> deleteUserByEmailUseCase.execute(command));
    }

    @Test
    void getUser_success_found() {
        var email = "test@gmail.com";

        var command = new DeleteUserByEmailCommand(
                email
        );

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(new UserModel(
                new UserId(),
                UserRole.CLIENT,
                email,
                "Encoded",
                "Test"
        )));

        deleteUserByEmailUseCase.execute(command);
    }

}

package com.card_management.user.usecase;

import com.card_management.shared.dto.UserRole;
import com.card_management.shared.id.UserId;
import com.card_management.user.application.exception.EmailAlreadyExists;
import com.card_management.user.application.usecase.CreateUserUseCase;
import com.card_management.user.application.usecase.command.CreateUserCommand;
import com.card_management.user.domain.UserModel;
import com.card_management.user.infrastructure.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTests {

    @Mock
    UserRepo repo;
    @Spy
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void createUser_success_forValidData() {
        var command = new CreateUserCommand(
                "Test",
                "Test12345678",
                "Test",
                UserRole.CLIENT
        );

        var userModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test",
                "Encoded",
                "Test"
        );

        Mockito.when(passwordEncoder.encode("Test12345678")).thenReturn("Encoded");
        Mockito.when(repo.save(Mockito.any(UserModel.class))).thenReturn(userModel);


        var result = createUserUseCase.execute(command);

        Assertions.assertEquals("Test", result.email());
        Assertions.assertNotNull(result.id());
        Assertions.assertEquals("Test", result.name());
        Mockito.verify(passwordEncoder).encode("Test12345678");
    }

    @Test
    void createUser_exception_blankEmail() {
        var command = new CreateUserCommand(
                " ",
                "Test12345678",
                "Test",
                UserRole.CLIENT
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(command));
    }

    @Test
    void createUser_exception_blankName() {
        var command = new CreateUserCommand(
                "Test",
                "Test12345678",
                " ",
                UserRole.CLIENT
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(command));
    }

    @Test
    void createUser_exception_smallPassword() {
        var command = new CreateUserCommand(
                "Test",
                "Test",
                " ",
                UserRole.CLIENT
        );

        Assertions.assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(command));
    }

    @Test
    void createUser_exception_alreadyExists() {
        var command = new CreateUserCommand(
                "Test",
                "Test12345678",
                "Test",
                UserRole.CLIENT
        );

        var userModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test",
                "Encoded",
                "Test"
        );

        Mockito.when(repo.findByEmail("Test")).thenReturn(Optional.of(userModel));

        Assertions.assertThrows(EmailAlreadyExists.class, () -> createUserUseCase.execute(command));
    }

}

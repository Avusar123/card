package com.bank.card.user.usecase;

import com.bank.card.user.application.exception.EmailAlreadyExists;
import com.bank.card.user.application.usecase.CreateUserUseCase;
import com.bank.card.user.application.usecase.command.CreateUserCommand;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTests {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Mock
    UserRepo repo;

    @Spy
    PasswordEncoder passwordEncoder;

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

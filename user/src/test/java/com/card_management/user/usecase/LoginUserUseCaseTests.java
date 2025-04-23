package com.card_management.user.usecase;

import com.card_management.shared.dto.UserRole;
import com.card_management.shared.id.UserId;
import com.card_management.user.application.usecase.LoginUserUseCase;
import com.card_management.user.application.usecase.command.LoginUserCommand;
import com.card_management.user.domain.UserModel;
import com.card_management.user.infrastructure.UserRepo;
import com.card_management.user.web.security.JwtGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoginUserUseCaseTests {

    @Mock
    UserRepo repo;
    @Spy
    PasswordEncoder passwordEncoder;
    @Mock
    JwtGenerator jwtService;
    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Test
    void loginUser_success_valid() {
        var password = "Test12345678";

        var email = "Test";

        var command = new LoginUserCommand(
                email,
                password
        );

        var userModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test",
                "Encoded",
                "Test"
        );

        Mockito.when(jwtService.generate(email)).thenReturn("Token");

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(userModel));

        Mockito.when(passwordEncoder.matches(password, userModel.getPasswordHash())).thenReturn(true);

        Assertions.assertEquals("Token", loginUserUseCase.execute(command));

    }

    @Test
    void loginUser_exception_mismatch() {
        var password = "Test12345678";

        var email = "Test";

        var command = new LoginUserCommand(
                email,
                password
        );

        var userModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test",
                "Encoded",
                "Test"
        );

        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(userModel));

        Assertions.assertThrows(BadCredentialsException.class, () -> loginUserUseCase.execute(command));

    }

    @Test
    void loginUser_exception_notFound() {
        var password = "Test12345678";

        var email = "Test";

        var command = new LoginUserCommand(
                email,
                password
        );

        Assertions.assertThrows(BadCredentialsException.class, () -> loginUserUseCase.execute(command));

    }

}

package com.card_management.user.usecase;

import com.card_management.shared.dto.UserRole;
import com.card_management.shared.id.UserId;
import com.card_management.user.application.usecase.GetUserByIdUseCase;
import com.card_management.user.application.usecase.command.GetUserByIdCommand;
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
public class GetUserByIdUseCaseTests {

    @Mock
    UserRepo repo;
    @InjectMocks
    private GetUserByIdUseCase getUserByIdUseCase;

    @Test
    void getUser_exception_notFound() {
        var command = new GetUserByIdCommand(
                new UserId()
        );

        Assertions.assertThrows(UsernameNotFoundException.class, () -> getUserByIdUseCase.execute(command));
    }

    @Test
    void getUser_success_found() {
        var id = new UserId();

        var command = new GetUserByIdCommand(
                id
        );

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(new UserModel(
                id,
                UserRole.CLIENT,
                "test@gmail.com",
                "Encoded",
                "Test"
        )));

        var result = getUserByIdUseCase.execute(command);

        Assertions.assertEquals(id, result.id());
    }

}

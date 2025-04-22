package com.bank.card.web.controller;

import com.bank.card.shared.dto.UserDto;
import com.bank.card.shared.id.UserId;
import com.bank.card.user.application.usecase.*;
import com.bank.card.user.application.usecase.command.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final DeleteUserByEmailUseCase deleteUserByEmailUseCase;
    private final GetUsersUseCase getUsersUseCase;

    @Autowired
    public UserController(CreateUserUseCase createUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          GetUserByEmailUseCase getUserByEmailUseCase,
                          GetUserByIdUseCase getUserByIdUseCase,
                          DeleteUserByEmailUseCase deleteUserByEmailUseCase,
                          GetUsersUseCase getUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.deleteUserByEmailUseCase = deleteUserByEmailUseCase;
        this.getUsersUseCase = getUsersUseCase;
    }

    @PostMapping("/user")
    public UserDto create(@RequestBody @Valid CreateUserCommand command) {
        return createUserUseCase.execute(command);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody @Valid LoginUserCommand command) {
        return loginUserUseCase.execute(command);
    }

    @GetMapping("/user")
    public UserDto getByEmail(@RequestParam(required = false) String email,
                              @RequestParam(required = false) UUID id) {
        if (email != null) {
            return getUserByEmailUseCase.execute(new GetUserByEmailCommand(email));
        } else if (id != null) {
            return getUserByIdUseCase.execute(new GetUserByIdCommand(new UserId(id)));
        }
        throw new IllegalArgumentException("Either email or id must be provided.");
    }

    @GetMapping("/user/all")
    public List<UserDto> all(@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero int page,
                             @RequestParam(name = "size", defaultValue = "5") @Positive int size) {
        return getUsersUseCase.execute(new GetUsersCommand(page, size));
    }

    @DeleteMapping("/user")
    public void delete(@RequestParam @NotBlank String email) {
        deleteUserByEmailUseCase.execute(new DeleteUserByEmailCommand(email));
    }
}

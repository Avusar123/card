package com.bank.card.web.controller;

import com.bank.card.user.application.usecase.*;
import com.bank.card.user.application.usecase.command.*;
import com.bank.card.user.application.usecase.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final DeleteUserByEmailUseCase deleteUserByEmailUseCase;
    private final GetUsersUseCase getUsersUseCase;

    @Autowired
    public UserController(CreateUserUseCase createUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          GetUserByEmailUseCase getUserByEmailUseCase,
                          DeleteUserByEmailUseCase deleteUserByEmailUseCase,
                          GetUsersUseCase getUsersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
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
    public UserDto get(@RequestParam String email) {
        return getUserByEmailUseCase.execute(new GetUserByEmailCommand(email));
    }

    @GetMapping("/user/all")
    public List<UserDto> all(@RequestParam(name = "page") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size) {
        return getUsersUseCase.execute(new GetUsersCommand(page, size));
    }

    @DeleteMapping("/user")
    public void delete(@RequestParam String email) {
        deleteUserByEmailUseCase.execute(new DeleteUserByEmailCommand(email));
    }
}

package com.bank.card.web.controller;

import com.bank.card.user.application.usecase.CreateUserUseCase;
import com.bank.card.user.application.usecase.GetUserByEmailUseCase;
import com.bank.card.user.application.usecase.LoginUserUseCase;
import com.bank.card.user.application.usecase.command.CreateUserCommand;
import com.bank.card.user.application.usecase.command.GetUserByEmailCommand;
import com.bank.card.user.application.usecase.command.LoginUserCommand;
import com.bank.card.user.application.usecase.dto.UserDto;
import com.bank.card.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;

    @Autowired
    public UserController(CreateUserUseCase createUserUseCase, LoginUserUseCase loginUserUseCase, GetUserByEmailUseCase getUserByEmailUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.getUserByEmailUseCase = getUserByEmailUseCase;
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
}

package com.card_management.user.application.usecase;

import com.card_management.shared.UseCase;
import com.card_management.user.application.usecase.command.LoginUserCommand;
import com.card_management.user.infrastructure.UserRepo;
import com.card_management.user.web.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
public class LoginUserUseCase {
    private static final RuntimeException authException = new BadCredentialsException("Email or password are wrong!");
    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    @Autowired
    public LoginUserUseCase(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String execute(@Valid LoginUserCommand command) {
        var email = command.email();

        var password = command.password();

        var user = userRepo.findByEmail(email).orElseThrow(() -> authException);

        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return jwtService.generate(email);
        } else {
            throw authException;
        }
    }
}

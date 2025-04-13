package com.bank.card.user.application.usecase.command;

import com.bank.card.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateUserCommand(@NotBlank @Email(message = "Email is not valid!") String email,
                                @NotBlank @Size(min = 8, message = "Password must have 8 or more digits") String password,
                                @NotBlank String name,
                                @NotNull UserRole role) {
}

package com.bank.card.user.application.usecase.command;

import com.bank.card.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateUserCommand(@NotBlank(message = "Email must not be blank") @Email(message = "Email is not valid!") String email,
                                @NotBlank(message = "Password must not be blank") @Size(min = 8, message = "Password must have 8 or more digits") String password,
                                @NotBlank(message = "Name must not be blank") String name,
                                @NotNull(message = "Role must be set") UserRole role) {
}

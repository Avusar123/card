package com.bank.card.user.application.usecase.command;

import com.bank.card.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(@NotBlank @Email String email,
                                @NotBlank @Size(min = 8) String password,
                                @NotBlank String name,
                                @NotNull UserRole role) {
}

package com.card_management.user.application.usecase.command;

import jakarta.validation.constraints.NotBlank;

public record LoginUserCommand(@NotBlank String email,
                               @NotBlank String password) {
}

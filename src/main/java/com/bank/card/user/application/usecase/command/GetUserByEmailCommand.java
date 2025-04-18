package com.bank.card.user.application.usecase.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GetUserByEmailCommand(@NotBlank @Email String email) {
}

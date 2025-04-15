package com.bank.card.user.application.usecase.command;

import com.bank.card.shared.id.UserId;
import jakarta.validation.constraints.NotNull;

public record GetUserByIdCommand(@NotNull(message = "User id must not be null") UserId id) {
}

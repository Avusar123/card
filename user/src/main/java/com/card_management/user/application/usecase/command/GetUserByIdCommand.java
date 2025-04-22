package com.card_management.user.application.usecase.command;

import com.card_management.shared.id.UserId;
import jakarta.validation.constraints.NotNull;

public record GetUserByIdCommand(@NotNull(message = "User id must not be null") UserId id) {
}

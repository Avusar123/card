package com.bank.card.card.application.usecase.command;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateCardCommand(@NotNull(message = "User id must not be null!") UUID userId,
                                @NotBlank(message = "Card number must not be blank!")
                                @Size(min = 16, max = 16)
                                @Pattern(regexp = "\\d+", message = "Card number must only contain numbers!") String cardNumber,
                                @PositiveOrZero int value,
                                @NotNull(message = "Expires must not be null!") @FutureOrPresent LocalDate expires) {
}

package com.card_management.card.application.usecase.command;

import jakarta.validation.constraints.NotBlank;

public record GetCardByNumberCommand(@NotBlank String rawNumber) {
}

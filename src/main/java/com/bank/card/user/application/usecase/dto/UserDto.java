package com.bank.card.user.application.usecase.dto;

import com.bank.card.user.domain.UserRole;

import java.util.UUID;

public record UserDto(UUID id, UserRole role, String email, String name) {
}

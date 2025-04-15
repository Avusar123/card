package com.bank.card.shared.dto;

import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record UserDto(@JsonUnwrapped UserId id, UserRole role, String email, String name) {
}

package com.card_management.shared.dto;

import com.card_management.shared.id.UserId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record UserDto(@JsonUnwrapped UserId id, UserRole role, String email, String name) {
}

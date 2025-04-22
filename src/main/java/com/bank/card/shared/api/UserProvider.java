package com.bank.card.shared.api;

import com.bank.card.shared.dto.UserDto;
import com.bank.card.shared.id.UserId;

public interface UserProvider {
    UserDto getById(UserId id);

    boolean exist(UserId id);
}

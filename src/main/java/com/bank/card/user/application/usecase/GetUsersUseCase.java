package com.bank.card.user.application.usecase;

import com.bank.card.user.application.usecase.command.GetUsersCommand;
import com.bank.card.user.application.usecase.dto.UserDto;
import com.bank.card.user.infrastructure.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetUsersUseCase {

    private final UserRepo repo;

    @Autowired
    public GetUsersUseCase(UserRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> execute(GetUsersCommand command) {
        var users = repo.findAll(PageRequest.of(command.page(), command.pageSize()));

        return users.map(user -> new UserDto(
                user.getId().uuid(),
                user.getRole(),
                user.getEmail(),
                user.getName())).toList();
    }
}

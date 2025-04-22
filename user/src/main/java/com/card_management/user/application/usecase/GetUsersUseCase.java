package com.card_management.user.application.usecase;

import com.card_management.shared.dto.UserDto;
import com.card_management.user.application.usecase.command.GetUsersCommand;
import com.card_management.user.infrastructure.UserRepo;
import jakarta.validation.Valid;
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
    public List<UserDto> execute(@Valid GetUsersCommand command) {
        var users = repo.findAll(PageRequest.of(command.page(), command.pageSize()));

        return users.map(user -> new UserDto(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getName())).toList();
    }
}

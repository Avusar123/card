package com.bank.card.shared.dto;

import com.bank.card.shared.id.UserId;
import com.bank.card.user.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {

    private final UserRole userRole;
    private final String password;
    private final String login;
    private final UserId id;

    public SecurityUser(UserRole userRole, String password, String login, UserId id) {
        this.userRole = userRole;
        this.password = password;
        this.login = login;
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.toString()));
    }

    public UserRole getRole() {
        return userRole;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public UserId getId() {
        return id;
    }
}

package com.card_management.user.application;

import com.card_management.user.infrastructure.UserRepo;
import com.card_management.web_security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    UserRepo userRepo;

    @Autowired
    public DefaultUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email was not found!");
        }

        var userModel = user.get();

        return new SecurityUser(
                userModel.getRole(),
                userModel.getPasswordHash(),
                userModel.getEmail(),
                userModel.getId());
    }
}

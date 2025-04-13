package com.bank.card.user.infrastructure;

import com.bank.card.user.domain.UserId;
import com.bank.card.user.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserModel, UserId> {
    Optional<UserModel> findByEmail(String email);
}

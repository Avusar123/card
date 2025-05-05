package com.card_management.user.infrastructure;

import com.card_management.shared.id.UserId;
import com.card_management.user.domain.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserModel, UserId> {
    Optional<UserModel> findByEmail(String email);
}

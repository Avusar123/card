package com.card_management.user.repo;

import com.card_management.shared.dto.UserRole;
import com.card_management.shared.id.UserId;
import com.card_management.user.domain.UserModel;
import com.card_management.user.infrastructure.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class UserRepoTests {
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void clearRepo() {
        userRepo.deleteAll();
    }

    @Test
    void createUser_success_uniqueEmail() {
        var id = new UserId();

        var userModel = new UserModel(
                id,
                UserRole.CLIENT,
                "Test@example.com",
                "Test",
                "Test"
        );

        userModel = userRepo.save(userModel);

        Assertions.assertEquals(id, userModel.getId());
    }

    @Test
    @Transactional
    void createUser_exception_notUniqueEmail() {
        var id = new UserId();

        var userModel = new UserModel(
                id,
                UserRole.CLIENT,
                "Test@example.com",
                "Test",
                "Test"
        );

        var newUserModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test@example.com",
                "Test",
                "Test"
        );

        userRepo.saveAndFlush(userModel);


        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepo.saveAndFlush(newUserModel));
    }


}

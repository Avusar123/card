package com.bank.card.user.repo;

import com.bank.card.user.domain.UserId;
import com.bank.card.user.domain.UserModel;
import com.bank.card.user.domain.UserRole;
import com.bank.card.user.infrastructure.UserRepo;
import com.bank.card.CardApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Isolation;
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
    void createUser_success() {
        var id = new UserId();

        var userModel = new UserModel(
                id,
                UserRole.CLIENT,
                "Test",
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
                "Test",
                "Test",
                "Test"
        );

        var newUserModel = new UserModel(
                new UserId(),
                UserRole.CLIENT,
                "Test",
                "Test",
                "Test"
        );

        userModel = userRepo.saveAndFlush(userModel);


        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepo.saveAndFlush(newUserModel));
    }


}

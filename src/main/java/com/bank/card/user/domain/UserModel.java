package com.bank.card.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

@Entity
public class UserModel {
    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id"))
    UserId id;

//    List<Card> cards;
//
//    List<Transaction> transactions;

    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(unique = true)
    String email;

    @Column(name = "password")
    String passwordHash;

    String name;

    UserModel() {}

    public UserModel(@NotNull UserId id,
                     @NotNull UserRole role,
                     @NotBlank String email,
                     @NotBlank String passwordHash,
                     @NotBlank String name) {
        Assert.notNull(id, "Id must not be null");

        Assert.notNull(role, "Role must not be null");

        Assert.notNull(email, "Email must not be null");

        Assert.isTrue(!email.trim().isEmpty(), "Email is empty");

        Assert.notNull(passwordHash, "Password is null");

        Assert.isTrue(!passwordHash.trim().isEmpty(), "Password is empty");

        Assert.notNull(name, "Name is null");

        Assert.isTrue(!name.trim().isEmpty(), "Name is empty");

        this.id = id;
        this.role = role;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

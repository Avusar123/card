package com.bank.card.user.domain;

public enum UserRole {
    CLIENT("CLIENT"),
    ADMIN("ADMIN");

    final String name;

    UserRole(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}

package com.card_management.shared.dto;

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

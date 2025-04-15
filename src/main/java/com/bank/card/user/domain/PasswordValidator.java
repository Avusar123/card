package com.bank.card.user.domain;

public final class PasswordValidator {
    private PasswordValidator() {
    }

    ;

    public static boolean valid(String password) {
        return password != null && !password.isBlank() && password.length() >= 8;
    }

    public static void throwIfInvalid(String password) {
        if (!valid(password))
            throw new IllegalArgumentException("Password must contains 8 or more digits");
    }
}

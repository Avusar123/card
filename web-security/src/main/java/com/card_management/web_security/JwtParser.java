package com.card_management.web_security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtParser {
    private final Key secret;

    public JwtParser(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtData parse(String token) {
        return new JwtData(token, secret);
    }
}

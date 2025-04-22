package com.card_management.card.web.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    private final Key secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtData parse(String token) {
        return new JwtData(token, secret);
    }
}

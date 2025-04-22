package com.card_management.user.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final Key secret;
    private final Integer expireSec;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") Integer expireSec) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
        this.expireSec = expireSec;
    }

    public String generate(String email) {
        return Jwts.builder()
                .expiration(Date.from(Instant.now().plusSeconds(expireSec)))
                .issuedAt(Date.from(Instant.now()))
                .subject(email)
                .signWith(secret)
                .compact();
    }

    public JwtData parse(String token) {
        return new JwtData(token, secret);
    }
}

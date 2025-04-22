package com.card_management.user.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;

public class JwtData {
    private Claims claims;

    JwtData(String token, Key signingKey) {
        parseClaims(token, signingKey);
    }

    private void parseClaims(String token, Key signingKey) {
        claims = Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isExpired() {
        return claims.getExpiration().toInstant().isBefore(Instant.now());
    }

    public String email() {
        return claims.getSubject();
    }
}

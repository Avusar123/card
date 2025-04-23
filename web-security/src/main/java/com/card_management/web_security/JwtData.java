package com.card_management.web_security;

import com.card_management.shared.dto.UserRole;
import com.card_management.shared.id.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.UUID;

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

    public UserRole role() {
        return UserRole.valueOf(claims.get("role", String.class));
    }

    public UserId id() {
        return new UserId(UUID.fromString(claims.getId()));
    }

    public String email() {
        return claims.getSubject();
    }

    public Claims getClaims() {
        return claims;
    }
}

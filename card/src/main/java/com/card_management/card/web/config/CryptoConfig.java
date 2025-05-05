package com.card_management.card.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class CryptoConfig {
    @Bean
    public Cipher cipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES");
    }

    @Bean
    public SecretKey secretKey(@Value("${cipher.secret}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);

        return new SecretKeySpec(decodedKey, "AES");
    }
}

package com.card_management.card.infrastructure;

import com.card_management.card.application.CardNumberEncoder;
import com.card_management.card.domain.CardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.util.Base64;

@Service
public class DefaultCardNumberEncoder implements CardNumberEncoder {
    private final Cipher cipher;
    private final SecretKey secretKey;

    @Autowired
    public DefaultCardNumberEncoder(Cipher cipher, SecretKey secretKey) {
        this.cipher = cipher;
        this.secretKey = secretKey;
    }

    @Override
    public CardNumber encode(String number) {
        try {
            if (number.length() != 16) {
                throw new IllegalArgumentException("Invalid card number length");
            }

            String masked = number.substring(0, 4) +
                    "*".repeat(number.length() - 8) +
                    number.substring(number.length() - 4);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            String secureNumber = Base64.getEncoder().encodeToString(cipher.doFinal(number.getBytes()));

            return new CardNumber(masked, secureNumber);

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decode(String hashed) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return new String(cipher.doFinal(Base64.getDecoder().decode(hashed)));

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}

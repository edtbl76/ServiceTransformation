package org.emangini.servolution.authz.jose;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtils {

    private static final String HMAC_SHA_256 = "HmacSha256";
    private static final String RSA = "RSA";

    private KeyGeneratorUtils() {}

    static SecretKey generateSecretKey() {
        SecretKey secretKey;

        try {
            secretKey = KeyGenerator.getInstance(HMAC_SHA_256).generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        return secretKey;
    }

    static KeyPair generateRsaKey() {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        return keyPair;
    }

}

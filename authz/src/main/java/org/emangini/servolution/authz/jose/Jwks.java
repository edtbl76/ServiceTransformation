package org.emangini.servolution.authz.jose;

import com.nimbusds.jose.jwk.RSAKey;


import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static java.util.UUID.randomUUID;
import static org.emangini.servolution.authz.jose.KeyGeneratorUtils.generateRsaKey;

public class Jwks {

    private Jwks() { }

    public static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(randomUUID().toString())
                .build();
    }

}

package com.jt.mgen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JiraUserJwtConfig {

    @Bean
    public KeyPair keyPair() throws Exception {
        // Read private key
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get("src/main/resources/private_key.pem"));
        privateKeyBytes = stripPemFormat(privateKeyBytes, "PRIVATE KEY");

        // Read public key
        byte[] publicKeyBytes = Files.readAllBytes(Paths.get("src/main/resources/public_key.pem"));
        publicKeyBytes = stripPemFormat(publicKeyBytes, "PUBLIC KEY");

        // Generate KeyPair
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    private byte[] stripPemFormat(byte[] keyBytes, String keyType) {
        String pem = new String(keyBytes);
        pem = pem.replace("-----BEGIN " + keyType + "-----", "")
                .replace("-----END " + keyType + "-----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(pem);
    }
}

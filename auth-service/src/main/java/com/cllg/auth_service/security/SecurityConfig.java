package com.cllg.auth_service.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SecurityConfig {


    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void debugJwtSecret() {
        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(
                            secret.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            String fingerprint = bytesToHex(hash)
                    .substring(0, 12);

            System.out.println(
                    "MENTOR JWT SECRET LENGTH = "
                            + secret.length()
            );

            System.out.println(
                    "MENTOR JWT SECRET FINGERPRINT = "
                            + fingerprint
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {

        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(
                    String.format("%02x", b)
            );
        }

        return result.toString();
    }
}

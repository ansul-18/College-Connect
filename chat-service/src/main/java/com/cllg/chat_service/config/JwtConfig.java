package com.cllg.chat_service.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    private final SecretKey signingKey;

    public JwtConfig(
            @Value("${jwt.secret}") String secret
    ) {

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                    "jwt.secret must contain at least 32 characters"
            );
        }

        this.signingKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    public SecretKey getSigningKey() {
        return signingKey;
    }
}
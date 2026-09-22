package com.cllg.chat_service.security;

import com.cllg.chat_service.config.JwtConfig;
import com.cllg.chat_service.dto.request.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(
                        jwtConfig.getSigningKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public AuthenticatedUser authenticate(
            String token
    ) {

        Claims claims = extractClaims(token);

        Object userIdClaim =
                claims.get("userId");

        Object roleClaim =
                claims.get("role");

        if (userIdClaim == null) {
            throw new IllegalArgumentException(
                    "userId claim missing"
            );
        }

        Long userId =
                Long.valueOf(
                        userIdClaim.toString()
                );

        String role =
                roleClaim != null
                        ? roleClaim.toString()
                        : "UNKNOWN";

        return new AuthenticatedUser(
                userId,
                normalizeRole(role)
        );
    }

    private String normalizeRole(
            String role
    ) {

        if (role == null) {
            return "UNKNOWN";
        }

        role = role.toUpperCase();

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        if ("USER".equals(role)) {
            return "STUDENT";
        }

        return role;
    }
}
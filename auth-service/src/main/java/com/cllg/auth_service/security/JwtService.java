package com.cllg.auth_service.security;

import com.cllg.auth_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;


    @PostConstruct
    public void debugJwtSecret() {
        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(secret.getBytes(StandardCharsets.UTF_8));

            String fingerprint = bytesToHex(hash)
                    .substring(0, 12);

            System.out.println(
                    "AUTH JWT SECRET LENGTH = " + secret.length()
            );

            System.out.println(
                    "AUTH JWT SECRET FINGERPRINT = " + fingerprint
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

    // =========================
    // Generate JWT Token
    // =========================
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());

        return generateToken(
                claims,
                user.getEmail()
        );
    }


    // =========================
    // Generic Token Generator
    // =========================
    private String generateToken(
            Map<String, Object> extraClaims,
            String username
    ) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(
                        new Date(
                                System.currentTimeMillis()
                        )
                )
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(getSigningKey(),
                        Jwts.SIG.HS384)     // ⭐ IMPORTANT
                .compact();
    }


    // =========================
    // Signing Key
    // =========================
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }


    // =========================
    // Extract Username
    // =========================
    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }


    // =========================
    // Extract User ID
    // =========================
    public Long extractUserId(String token) {

        return extractClaim(
                token,
                claims ->
                        claims.get(
                                "userId",
                                Long.class
                        )
        );
    }


    // =========================
    // Extract Role
    // =========================
    public String extractRole(String token) {

        return extractClaim(
                token,
                claims ->
                        claims.get(
                                "role",
                                String.class
                        )
        );
    }


    // =========================
    // Generic Claim Extractor
    // =========================
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims =
                extractAllClaims(token);

        return claimsResolver.apply(claims);
    }


    // =========================
    // Parse Signed JWT
    // =========================
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // =========================
    // Validate JWT
    // =========================
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        try {

            String username =
                    extractUsername(token);

            return username.equals(
                    userDetails.getUsername()
            )
                    && !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }


    // =========================
    // Check Expiration
    // =========================
    private boolean isTokenExpired(
            String token
    ) {

        return extractExpiration(token)
                .before(new Date());
    }


    // =========================
    // Extract Expiration
    // =========================
    public Date extractExpiration(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }


    // =========================
    // Get Expiration
    // =========================
    public long getJwtExpiration() {

        return jwtExpiration;
    }
}
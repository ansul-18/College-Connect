package com.cllg.mentor_service.config;

import javax.crypto.spec.SecretKeySpec;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void debugJwtSecret() {
        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(
                            jwtSecret.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            String fingerprint = bytesToHex(hash)
                    .substring(0, 12);

            System.out.println(
                    "MENTOR JWT SECRET LENGTH = "
                            + jwtSecret.length()
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

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        jwtSecret.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA384"
                );

        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC MENTOR GET APIs
                        // =========================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/mentors",
                                "/api/mentors/*",
                                "/api/mentors/department/**",
                                "/api/mentors/type/**",
                                "/api/mentors/search",
                                "/api/mentors/*/user-id",
                                "/api/mentors/by-user/*/id"
                        )
                        .permitAll()

                        // =========================
                        // MENTOR CREATES OWN PROFILE
                        // =========================
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/mentors"

                        )
                        .hasAuthority("ROLE_MENTOR")

                        // =========================
                        // ADMIN MENTOR MANAGEMENT
                        // =========================
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/mentors/*"
                        )
                        .hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_MENTOR"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/mentors/*/deactivate"
                        )
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/mentors/*"
                        )
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/actuator/health")
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt ->
                                                jwt.jwtAuthenticationConverter(
                                                        jwtAuthenticationConverter()
                                                )
                                )
                );

        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setPrincipalClaimName("userId");

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role = jwt.getClaimAsString("role");

            if (role == null || role.isBlank()) {
                return List.of();
            }

            return List.of(
                    new SimpleGrantedAuthority(
                            "ROLE_" + role
                    )
            );
        });

        return converter;
    }
}
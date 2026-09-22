//package com.cllg.college_service.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//
//import java.util.List;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(
//            HttpSecurity http
//    ) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(
//                                SessionCreationPolicy.STATELESS
//                        )
//                )
//
//                .authorizeHttpRequests(auth -> auth
//
//                        // =========================
//                        // PUBLIC GET APIs
//                        // =========================
//
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/college/events",
//                                "/api/college/events/{id}",
//                                "/api/college/events/status/{status}",
//                                "/api/college/announcements",
//                                "/api/college/announcements/{id}"
//                        ).permitAll()
//
//
//                        // =========================
//                        // ADMIN - EVENTS
//                        // =========================
//
//                        .requestMatchers(
//                                HttpMethod.POST,
//                                "/api/college/events"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//                                HttpMethod.PUT,
//                                "/api/college/events/{id}"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//                                HttpMethod.DELETE,
//                                "/api/college/events/{id}"
//                        ).hasRole("ADMIN")
//
//
//                        // =========================
//                        // ADMIN - ANNOUNCEMENTS
//                        // =========================
//
//                        .requestMatchers(
//                                HttpMethod.POST,
//                                "/api/college/announcements"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//                                HttpMethod.PUT,
//                                "/api/college/announcements/{id}"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//                                HttpMethod.DELETE,
//                                "/api/college/announcements/{id}"
//                        ).hasRole("ADMIN")
//
//
//                        // =========================
//                        // STUDENT - EVENT REGISTRATION
//                        // =========================
//
//                        .requestMatchers(
//                                HttpMethod.POST,
//                                "/api/college/events/*/register/*"
//                        ).hasRole("STUDENT")
//
//                        .requestMatchers(
//                                HttpMethod.DELETE,
//                                "/api/college/events/*/register/*"
//                        ).hasRole("STUDENT")
//
//                        // =========================
//                        // REGISTRATIONS
//                        // =========================
//
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/college/events/*/registrations"
//                        ).hasRole("ADMIN")
//
//                        .requestMatchers(
//                                HttpMethod.GET,
//                                "/api/college/events/student/*"
//                        ).hasRole("STUDENT")
//
//
//                        // =========================
//                        // ACTUATOR
//                        // =========================
//
//                        .requestMatchers(
//                                "/actuator/health",
//                                "/actuator/info"
//                        ).permitAll()
//
//                        .anyRequest().authenticated()
//                )
//
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwt ->
//                                jwt.jwtAuthenticationConverter(
//                                        jwtAuthenticationConverter()
//                                )
//                        )
//                );
//
//        return http.build();
//    }
//
//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//
//        JwtAuthenticationConverter converter =
//                new JwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(
//                jwt -> {
//
//                    String role =
//                            jwt.getClaimAsString("role");
//
//                    if (role == null || role.isBlank()) {
//                        return List.of();
//                    }
//
//                    return List.of(
//                            new org.springframework.security.core.authority.SimpleGrantedAuthority(
//                                    "ROLE_" + role
//                            )
//                    );
//                }
//        );
//
//        return converter;
//    }
//}
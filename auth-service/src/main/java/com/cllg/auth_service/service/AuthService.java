package com.cllg.auth_service.service;

import com.cllg.auth_service.dto.AuthResponse;
import com.cllg.auth_service.dto.LoginRequest;
import com.cllg.auth_service.dto.RegisterRequest;
import com.cllg.auth_service.dto.UserResponse;
import com.cllg.auth_service.enums.Role;
import com.cllg.auth_service.entity.User;
import com.cllg.auth_service.repository.UserRepository;
import com.cllg.auth_service.security.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    // =========================
    // NORMAL STUDENT REGISTER
    // =========================
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.STUDENT)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        String token =
                jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .token(token)
                .build();
    }

    // =========================
    // LOGIN
    // =========================
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        String token =
                jwtService.generateToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    public AuthResponse makeMentor(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found with id: " + userId
                        )
                );

        if (user.getRole() == Role.MENTOR) {
            throw new RuntimeException(
                    "User is already a mentor"
            );
        }

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException(
                    "Only a student can be converted to mentor"
            );
        }

        user.setRole(Role.MENTOR);

        User updatedUser =
                userRepository.save(user);

        String token =
                jwtService.generateToken(updatedUser);

        return AuthResponse.builder()
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .token(token)
                .build();
    }

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void debugJwtSecret() {
        System.out.println(
                "JWT SECRET LENGTH = " + secret.length()
        );
    }

    public void resetPassword(
            Long userId,
            String newPassword
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
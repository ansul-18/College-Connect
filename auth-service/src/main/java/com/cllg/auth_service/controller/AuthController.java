package com.cllg.auth_service.controller;

import com.cllg.auth_service.dto.AuthResponse;
import com.cllg.auth_service.dto.LoginRequest;
import com.cllg.auth_service.dto.RegisterRequest;
import com.cllg.auth_service.dto.UserResponse;
import com.cllg.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor

public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PatchMapping("/admin/mentors/{userId}")
    public ResponseEntity<AuthResponse> makeMentor(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                authService.makeMentor(userId)
        );
    }

    @PutMapping("/admin/reset-password/{userId}")
    public ResponseEntity<String> resetPassword(
            @PathVariable Long userId,
            @RequestParam String newPassword
    ) {
        authService.resetPassword(userId, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                authService.getUserById(id)
        );
    }
}

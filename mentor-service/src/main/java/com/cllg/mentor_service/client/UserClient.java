package com.cllg.mentor_service.client;

import com.cllg.mentor_service.dto.response.UserResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "AUTH-SERVICE"
)
public interface UserClient {

    @GetMapping("/api/auth/users/{id}")
    UserResponse getUserById(
            @PathVariable("id") Long id
    );
}
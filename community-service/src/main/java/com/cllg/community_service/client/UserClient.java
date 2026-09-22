package com.cllg.community_service.client;

import com.cllg.community_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "USER-SERVICE"
)
public interface UserClient {

    @GetMapping(
            "/api/users/students/{id}"
    )
    UserResponse getStudentById(
            @PathVariable("id") Long id
    );
}
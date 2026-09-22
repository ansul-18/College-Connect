package com.cllg.mentor_service.client;

import com.cllg.mentor_service.dto.response.DepartmentResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "DEPARTMENT-SERVICE"
)
public interface DepartmentClient {

    @GetMapping(
            "/api/departments/{id}"
    )
    DepartmentResponse getDepartmentById(
            @PathVariable("id") Long id
    );
}
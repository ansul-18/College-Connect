package com.cllg.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentClient {

    @GetMapping("/api/departments/{id}")
     DepartmentResponse getDepartmentById(@PathVariable("id") Long departmentId);

    @GetMapping("/api/departments/code/{code}")
    DepartmentResponse getDepartmentByCode(
            @PathVariable("code") String code
    );
}

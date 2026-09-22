package com.cllg.college_service.client;

import com.cllg.college_service.dto.response.DepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentClient {

    @GetMapping("/api/departments/{id}" )
    DepartmentResponse getDepartmentById(
            @PathVariable("id") Long id
    );
}

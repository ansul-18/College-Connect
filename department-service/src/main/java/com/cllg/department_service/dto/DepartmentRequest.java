package com.cllg.department_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(max = 50, message = "Department name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Department code is required")
    @Size(max = 10,message = "Department code cannot exceed 20 characters")
    private String code;

}

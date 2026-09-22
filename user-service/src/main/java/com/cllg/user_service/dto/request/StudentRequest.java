package com.cllg.user_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentRequest {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email")
    private String email;
    @Size(max = 20)
    private String mobile;
    @NotBlank(message = "Roll number is required")
    @Size(max = 50)
    private String rollNumber;

    @NotNull(message = "Department is required")
    private Long departmentId;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year must be between 1 and 4")
    @Max(value = 4, message = "Year must be between 1 and 4")
    private Integer year;

    @Size(max = 500)
    private String profileImage;
    @Size(max = 1000)
    private String bio;
    @Size(max = 500)
    private String linkedin;
    @Size(max = 500)
    private String github;

}

package com.cllg.mentor_service.dto.request;

import com.cllg.mentor_service.enums.MentorType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MentorRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    private Long departmentId;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year must be between 1 and 4")
    @Max(value = 4, message = "Year must be between 1 and 4")
    private Integer year;

    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @Size(max = 100, message = "Experience cannot exceed 100 characters")
    private String experience;

    @Size(max = 150, message = "Company cannot exceed 150 characters")
    private String company;

    @Size(max = 150, message = "Designation cannot exceed 150 characters")
    private String designation;

    @Size(max = 2000, message = "Bio cannot exceed 2000 characters")
    private String bio;

    private String profileImage;

    @Size(max = 500, message = "GitHub URL is too long")
    private String github;

    @Size(max = 500, message = "LinkedIn URL is too long")
    private String linkedin;

    @NotNull(message = "Mentor type is required")
    private MentorType mentorType;

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Price cannot be negative"
    )
    private BigDecimal price;

    private List<
            @NotBlank(message = "Skill cannot be blank")
                    String
            > skills;
}
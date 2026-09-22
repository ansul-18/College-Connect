package com.cllg.college_service.dto.request;

import com.cllg.college_service.enums.TargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.Primary;

@Data
public class AnnouncementRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    private Long departmentId;
    @NotNull(message = "Target type is required")
    private TargetType targetType;
    @NotNull(message = "Created by is required")
    private Long createdBy;
}

package com.cllg.college_service.dto.request;

import com.cllg.college_service.enums.EventCategory;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.enums.TargetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;


    @NotBlank(message = "Description is required")
    private String description;


    private Long departmentId;


    @NotNull(message = "Target type is required")
    private TargetType targetType;


    @NotNull(message = "Category is required")
    private EventCategory category;


    @NotNull(message = "Date is required")
    private LocalDate date;


    @NotNull(message = "Time is required")
    private LocalTime time;


    @Size(max = 200)
    private String venue;


    @NotNull(message = "Registration required flag is required")
    private Boolean registrationRequired;


    @Min(value = 1, message = "Registration limit must be positive")
    private Integer registrationLimit;


    private EventStatus status;


    @NotNull(message = "Created by is required")
    private Long createdBy;
}

package com.cllg.college_service.dto.response;

import com.cllg.college_service.enums.EventCategory;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.enums.TargetType;
import lombok.*;
import org.hibernate.query.criteria.JpaEntityJoin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Long departmentId;

    private TargetType targetType;

    private EventCategory category;

    private LocalDate date;

    private LocalTime time;

    private String venue;

    private Boolean registrationRequired;

    private Integer registrationLimit;

    private EventStatus status;

    private Long createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long registrationCount;
}

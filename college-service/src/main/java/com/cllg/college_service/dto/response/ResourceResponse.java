package com.cllg.college_service.dto.response;

import com.cllg.college_service.enums.ResourceCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceResponse {

    private Long id;

    private String title;

    private String description;

    private String fileUrl;

    private String fileType;

    private ResourceCategory category;

    private Long departmentId;

    private Integer year;

    private Long uploadedBy;

    private LocalDateTime createdAt;
}

package com.cllg.college_service.dto.response;

import com.cllg.college_service.enums.ComplaintCategory;
import com.cllg.college_service.enums.ComplaintStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponse {

    private Long id;

    private Long studentId;

    private Long departmentId;

    private String title;

    private String description;

    private ComplaintCategory category;

    private String location;

    private String imageUrl;

    private ComplaintStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

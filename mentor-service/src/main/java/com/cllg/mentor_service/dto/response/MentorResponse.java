package com.cllg.mentor_service.dto.response;

import com.cllg.mentor_service.enums.MentorType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorResponse {

    private Long id;

    // Auth User
    private Long userId;
    private String name;
    private String email;

    // Academic
    private Long departmentId;
    private String departmentName;
    private Integer year;

    // Professional
    private String title;
    private String experience;
    private String company;
    private String designation;

    // Profile
    private String bio;
    private String profileImage;

    // Skills
    private List<String> skills;

    // Social
    private String github;
    private String linkedin;

    // Mentorship
    private MentorType mentorType;
    private BigDecimal price;

    // Account
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
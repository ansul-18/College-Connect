package com.cllg.user_service.dto.response;

import com.cllg.user_service.enums.AccountStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private Long authUserId;
    private String name;
    private String email;
    private String mobile;
    private String rollNumber;
    private Long departmentId;
    private String departmentName;
    private Integer year;
    private String profileImage;
    private String bio;
    private String github;
    private String linkedin;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

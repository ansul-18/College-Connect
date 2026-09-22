package com.cllg.community_service.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private Long authUserId;

    private String name;

    private String email;

    private String mobile;

    private String rollNumber;

    private Long departmentId;

    private Integer year;

    private String profileImage;

    private String bio;

    private String github;

    private String linkedin;

    private String accountStatus;
}
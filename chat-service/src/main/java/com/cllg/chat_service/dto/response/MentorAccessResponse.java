package com.cllg.chat_service.dto.response;

import lombok.Data;

@Data
public class MentorAccessResponse {

    private Long studentId;

    private Long mentorId;

    private String status;

    private Boolean chatAllowed;
}
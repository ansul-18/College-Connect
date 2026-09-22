package com.cllg.mentor_service.service;

import com.cllg.mentor_service.dto.response.MentorAccessResponse;

public interface MentorAccessService {

    MentorAccessResponse getAccess(
            Long studentId,
            Long mentorId
    );

    boolean canChat(
            Long studentId,
            Long mentorId
    );
}
package com.cllg.mentor_service.service;

import com.cllg.mentor_service.dto.request.MentorRequest;
import com.cllg.mentor_service.dto.response.MentorResponse;
import com.cllg.mentor_service.enums.MentorType;

import java.util.List;

public interface MentorService {

    MentorResponse create(
            MentorRequest request
    );

    MentorResponse getById(
            Long id
    );

    List<MentorResponse> getAll();

    List<MentorResponse> getByDepartment(
            Long departmentId
    );

    List<MentorResponse> getByType(
            MentorType mentorType
    );

    List<MentorResponse> search(
            String skill
    );

    MentorResponse update(
            Long id,
            MentorRequest request
    );

    void deactivate(
            Long id
    );

    void delete(
            Long id
    );
    Long getUserId(Long mentorId);
    Long getMentorProfileIdByUserId(Long userId);

    boolean hasChatAccess(
            Long studentId,
            Long mentorId
    );
}

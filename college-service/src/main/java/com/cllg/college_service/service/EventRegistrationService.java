package com.cllg.college_service.service;

import com.cllg.college_service.dto.response.RegistrationResponse;

import java.util.List;

public interface EventRegistrationService {
    RegistrationResponse register(Long eventId, Long studentId);

    void cancel(Long eventId, Long studentId);

    List<RegistrationResponse> getByEvent(Long eventId);

    List<RegistrationResponse> getByStudent(Long studentId);
}

package com.cllg.college_service.service;

import com.cllg.college_service.dto.request.EventRequest;
import com.cllg.college_service.dto.response.EventResponse;
import com.cllg.college_service.enums.EventStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventResponse create(EventRequest request, MultipartFile file);
    EventResponse getById(Long id);
    List<EventResponse> getAll();
    List<EventResponse> getByStatus(EventStatus status);
    EventResponse update(Long id, EventRequest request, MultipartFile image);
    void delete(Long id);
}

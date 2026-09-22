package com.cllg.college_service.service;

import com.cllg.college_service.dto.request.AnnouncementRequest;
import com.cllg.college_service.dto.response.AnnouncementResponse;
import com.cllg.college_service.entity.Announcement;
import com.cllg.college_service.repository.AnnouncementRepository;

import java.lang.reflect.AccessibleObject;
import java.util.List;

public interface AnnouncementService {

    AnnouncementResponse create(AnnouncementRequest request);
    AnnouncementResponse getById(Long id);
    List<AnnouncementResponse> getAll();
    AnnouncementResponse update(Long id,AnnouncementRequest request);
    void delete(Long id);

}

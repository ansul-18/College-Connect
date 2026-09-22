package com.cllg.college_service.service;

import com.cllg.college_service.dto.response.ResourceResponse;
import com.cllg.college_service.enums.ResourceCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {

    ResourceResponse upload(
            String title,
            String description,
            ResourceCategory category,
            Long departmentId,
            Integer year,
            Long uploadedBy,
            MultipartFile file
    );

    List<ResourceResponse> getAll();

    List<ResourceResponse> getByCategory(ResourceCategory category);

    List<ResourceResponse> getByDepartment(Long departmentId);

    List<ResourceResponse> getByYear(Integer year);

    void delete(Long id);
}

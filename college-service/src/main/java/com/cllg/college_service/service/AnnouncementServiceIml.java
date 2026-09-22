package com.cllg.college_service.service;

import com.cllg.college_service.client.DepartmentClient;
import com.cllg.college_service.dto.request.AnnouncementRequest;
import com.cllg.college_service.dto.response.AnnouncementResponse;
import com.cllg.college_service.dto.response.DepartmentResponse;
import com.cllg.college_service.entity.Announcement;
import com.cllg.college_service.enums.TargetType;
import com.cllg.college_service.exception.ResourceNotFoundException;
import com.cllg.college_service.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementServiceIml implements AnnouncementService{

    private final AnnouncementRepository announcementRepository;
    private final DepartmentClient departmentClient;


    // CREATE
    @Override
    public AnnouncementResponse create(AnnouncementRequest request) {
        validateTarget(request.getTargetType(),request.getDepartmentId());

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .departmentId(request.getDepartmentId())
                .targetType(request.getTargetType())
                .createdBy(request.getCreatedBy())
                .build();
        Announcement saved = announcementRepository.save(announcement);
        return mapToResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse getById(Long id) {
        return mapToResponse(announcementRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Announcement not found: " + id
                )));
    }


    // RESPONSE
    private AnnouncementResponse mapToResponse(Announcement entity) {
        return AnnouncementResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .departmentId(entity.getDepartmentId())
                .targetType(entity.getTargetType())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    //VALIDATION
    private void validateTarget(TargetType targetType, Long departmentId) {

        if (targetType == TargetType.DEPARTMENT) {

            if (departmentId == null) {
                throw new IllegalArgumentException("Department is required for DEPARTMENT target");
            }

            validateDepartment(departmentId);
        }

    }

    private void validateDepartment(Long departmentId) {

        try {

            DepartmentResponse response = departmentClient.getDepartmentById(departmentId);

            if (response == null) {

                throw new ResourceNotFoundException("Department not found with id: " + departmentId);
            }

        } catch (Exception ex) {

            throw new ResourceNotFoundException("Unable to validate department: " + departmentId);
        }
    }


    // GET ALL STUDENT
    @Override
    public List<AnnouncementResponse> getAll() {
        return announcementRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(announcement -> mapToResponse(announcement))
                .toList();
    }

    // UPDATE
    @Override
    public AnnouncementResponse update(Long id, AnnouncementRequest request) {
        validateTarget(request.getTargetType(),request.getDepartmentId());

        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Announcement not found: " + id));

        announcement.setTitle(request.getTitle().trim());
        announcement.setDescription(request.getDescription());
        announcement.setDepartmentId(request.getDepartmentId());
        announcement.setTargetType(request.getTargetType());

        return mapToResponse(announcementRepository.save(announcement));
    }

    @Override
    public void delete(Long id) {

        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Announcement not found: " + id
                ) );

        announcementRepository.delete(announcement);
    }
}

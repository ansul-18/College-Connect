package com.cllg.college_service.service;

import com.cllg.college_service.client.DepartmentClient;
import com.cllg.college_service.dto.response.DepartmentResponse;
import com.cllg.college_service.dto.response.ResourceResponse;
import com.cllg.college_service.entity.Resource;
import com.cllg.college_service.enums.ResourceCategory;
import com.cllg.college_service.exception.ResourceNotFoundException;
import com.cllg.college_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceServiceImpl implements ResourceService{

    private final ResourceRepository resourceRepository;

    private final DepartmentClient departmentClient;

    private final CloudinaryService cloudinaryService;



    @Override
    public ResourceResponse upload(String title, String description, ResourceCategory category, Long departmentId, Integer year, Long uploadedBy, MultipartFile file) {
        if (year == null || year < 1 || year > 4) {

            throw new IllegalArgumentException("Year must be between 1 and 4");
        }


        if (departmentId != null) {
            validateDepartment(departmentId);
        }


        String fileUrl = cloudinaryService
                        .uploadRawFile(file, "college-connect/resources");


        Resource resource = Resource.builder()

                        .title(title.trim())
                        .description(description)
                        .fileUrl(fileUrl)
                        .filetype(file.getContentType())
                        .resourceCategory(category)
                        .departmentId(departmentId)
                        .year(year)
                        .uploadedBy(uploadedBy)
                        .build();


        return mapToResponse(resourceRepository.save(resource));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getAll() {
        return resourceRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getByCategory(ResourceCategory category) {
        return resourceRepository
                .findByResourceCategoryOrderByCreatedAtDesc(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ResourceResponse> getByDepartment(Long departmentId) {
        if (departmentId != null) {
            validateDepartment(departmentId);
        }

        return resourceRepository
                .findByDepartmentIdOrderByCreatedAtDesc(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> getByYear(Integer year) {
        if (year == null || year < 1 || year > 4) {

            throw new IllegalArgumentException("Year must be between 1 and 4");
        }


        return resourceRepository
                .findByYearOrderByCreatedAtDesc(year)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        Resource resource =
                resourceRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Resource not found: "
                                                        + id
                                        )
                        );

        resourceRepository.delete(
                resource
        );
    }

    private void validateDepartment(
            Long departmentId
    ) {

        try {

            DepartmentResponse response =
                    departmentClient.getDepartmentById(
                            departmentId
                    );


            if (response == null) {

                throw new ResourceNotFoundException(
                        "Department not found with id: "
                                + departmentId
                );
            }

        } catch (ResourceNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ResourceNotFoundException(
                    "Unable to validate department: "
                            + departmentId
            );
        }
    }

    // =====================================================
    // ENTITY TO RESPONSE
    // =====================================================

    private ResourceResponse mapToResponse(
            Resource entity
    ) {

        return ResourceResponse.builder()

                .id(
                        entity.getId()
                )

                .title(
                        entity.getTitle()
                )

                .description(
                        entity.getDescription()
                )

                .fileUrl(
                        entity.getFileUrl()
                )

                .fileType(
                        entity.getFiletype()
                )

                .category(
                        entity.getResourceCategory()
                )

                .departmentId(
                        entity.getDepartmentId()
                )

                .year(
                        entity.getYear()
                )

                .uploadedBy(
                        entity.getUploadedBy()
                )

                .createdAt(
                        entity.getCreatedAt()
                )

                .build();
    }
}

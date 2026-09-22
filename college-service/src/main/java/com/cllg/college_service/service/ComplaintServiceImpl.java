package com.cllg.college_service.service;

import com.cllg.college_service.client.DepartmentClient;
import com.cllg.college_service.dto.request.ComplaintRequest;
import com.cllg.college_service.dto.response.ComplaintResponse;
import com.cllg.college_service.dto.response.DepartmentResponse;
import com.cllg.college_service.entity.Complaint;
import com.cllg.college_service.enums.ComplaintStatus;
import com.cllg.college_service.exception.ResourceNotFoundException;
import com.cllg.college_service.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintServiceImpl implements ComplaintService{

    private final ComplaintRepository complaintRepository;

    private final DepartmentClient departmentClient;

    private final CloudinaryService cloudinaryService;

    @Override
    public ComplaintResponse create(ComplaintRequest request, MultipartFile image) {


        if (request.getDepartmentId() != null) {
            validateDepartment(request.getDepartmentId());
        }

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {

            imageUrl = cloudinaryService.uploadImage(image, "college-connect/complaints");
        }


        Complaint complaint = Complaint.builder()

                        .studentId(request.getStudentId())
                        .departmentId(request.getDepartmentId())
                        .title(request.getTitle().trim())
                        .description(request.getDescription())
                        .complaintCategory(request.getCategory())
                        .location(request.getLocation())
                        .imageUrl(imageUrl).complaintStatus(ComplaintStatus.PENDING)
                        .build();


        return mapToResponse(complaintRepository.save(complaint));
    }


    @Override
    @Transactional(readOnly = true)
    public ComplaintResponse getById(Long id) {

        return mapToResponse(complaintRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Complaint not found: " + id))
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponse>
    getMyComplaints(Long studentId) {

        return complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponse>
    getAll() {

        return complaintRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponse>
    getByStatus(
            ComplaintStatus status) {

        return complaintRepository
                .findByComplaintStatusOrderByCreatedAtDesc(
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public ComplaintResponse updateStatus(
            Long id,
            ComplaintStatus status) {

        Complaint complaint =
                complaintRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Complaint not found: "
                                                        + id
                                        )
                        );


        complaint.setComplaintStatus(status);


        return mapToResponse(
                complaintRepository.save(
                        complaint
                )
        );
    }


    private void validateDepartment(Long departmentId) {

        try {

            DepartmentResponse response = departmentClient
                            .getDepartmentById(departmentId);


        } catch (ResourceNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ResourceNotFoundException(
                    "Unable to validate department: "
                            + departmentId
            );
        }
    }


    private ComplaintResponse mapToResponse(
            Complaint entity) {

        return ComplaintResponse
                .builder()

                .id(entity.getId())

                .studentId(
                        entity.getStudentId()
                )

                .departmentId(
                        entity.getDepartmentId()
                )

                .title(entity.getTitle())

                .description(
                        entity.getDescription()
                )

                .category(
                        entity.getComplaintCategory()
                )

                .location(
                        entity.getLocation()
                )

                .imageUrl(
                        entity.getImageUrl()
                )

                .status(
                        entity.getComplaintStatus()
                )

                .createdAt(
                        entity.getCreatedAt()
                )

                .updatedAt(
                        entity.getUpdatedAt()
                )

                .build();
    }
}

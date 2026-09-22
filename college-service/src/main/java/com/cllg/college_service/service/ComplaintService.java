package com.cllg.college_service.service;

import com.cllg.college_service.dto.request.ComplaintRequest;
import com.cllg.college_service.dto.response.ComplaintResponse;
import com.cllg.college_service.enums.ComplaintStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ComplaintService {

    ComplaintResponse create(
            ComplaintRequest request,
            MultipartFile image
    );

    ComplaintResponse getById(
            Long id
    );

    List<ComplaintResponse> getMyComplaints(
            Long studentId
    );

    List<ComplaintResponse> getAll();

    List<ComplaintResponse> getByStatus(
            ComplaintStatus status
    );

    ComplaintResponse updateStatus(
            Long id,
            ComplaintStatus status
    );
}
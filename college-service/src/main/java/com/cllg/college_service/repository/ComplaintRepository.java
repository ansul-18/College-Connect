package com.cllg.college_service.repository;

import com.cllg.college_service.entity.Complaint;
import com.cllg.college_service.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    List<Complaint>
    findByStudentIdOrderByCreatedAtDesc(
            Long studentId
    );


    List<Complaint>
    findAllByOrderByCreatedAtDesc();

    List<Complaint>
    findByComplaintStatusOrderByCreatedAtDesc(
            ComplaintStatus complaintStatus
    );
}

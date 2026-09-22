package com.cllg.mentor_service.repository;

import com.cllg.mentor_service.entity.MentorAccess;
import com.cllg.mentor_service.enums.MentorAccessStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorAccessRepository
        extends JpaRepository<MentorAccess, Long> {

    Optional<MentorAccess>
    findByStudentIdAndMentorId(
            Long studentId,
            Long mentorId
    );

    Optional<MentorAccess>
    findByRazorpayOrderId(
            String razorpayOrderId
    );

    boolean
    existsByStudentIdAndMentorIdAndStatus(
            Long studentId,
            Long mentorId,
            MentorAccessStatus status
    );
}
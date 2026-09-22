package com.cllg.mentor_service.repository;

import com.cllg.mentor_service.entity.MentorProfile;
import com.cllg.mentor_service.enums.MentorType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorProfileRepository
        extends JpaRepository<MentorProfile, Long> {

    boolean existsByUserId(Long userId);

    List<MentorProfile> findByActiveTrue();

    List<MentorProfile>
    findByDepartmentIdAndActiveTrue(
            Long departmentId
    );

    List<MentorProfile>
    findByMentorTypeAndActiveTrue(
            MentorType mentorType
    );
    Optional<MentorProfile> findByUserId(Long userId);
}
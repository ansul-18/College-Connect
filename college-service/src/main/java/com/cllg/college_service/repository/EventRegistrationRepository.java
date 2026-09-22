package com.cllg.college_service.repository;

import com.cllg.college_service.entity.EventRegistration;
import com.cllg.college_service.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Long> {

    Optional<EventRegistration> findByEventIdAndStudentId(Long eventId,Long studentId);
    boolean existsByEventIdAndStudentId(Long eventId, Long studentId);
    long countByEventId(Long eventId);
    long countByEventIdAndStatus(Long eventId, RegistrationStatus status);
    List<EventRegistration> findByEventIdOrderByRegisteredAtDesc(Long eventId);
    List<EventRegistration> findByStudentIdOrderByRegisteredAtDesc(Long studentId);

}

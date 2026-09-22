package com.cllg.college_service.repository;

import com.cllg.college_service.entity.Event;
import com.cllg.college_service.enums.EventCategory;
import com.cllg.college_service.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventStatusOrderByDateAscTimeAsc(EventStatus eventStatus);
    List<Event> findByDepartmentIdOrderByDateAscTimeAsc(Long departmentId);
    List<Event> findByEventCategoryOrderByDateAscTimeAsc(EventCategory eventCategory);
    List<Event> findByDateOrderByTimeAsc(LocalDate date);
    List<Event> findAllByOrderByCreatedAtDesc();
}

package com.cllg.college_service.repository;

import com.cllg.college_service.entity.Announcement;
import com.cloudinary.provisioning.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement>
    findAllByOrderByCreatedAtDesc();

}

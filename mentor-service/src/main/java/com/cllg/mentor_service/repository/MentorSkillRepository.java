package com.cllg.mentor_service.repository;

import com.cllg.mentor_service.entity.MentorSkill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MentorSkillRepository
        extends JpaRepository<MentorSkill, Long> {

    List<MentorSkill>
    findByMentorId(Long mentorId);

    @Modifying(flushAutomatically = true)
    @Query("""
            DELETE FROM MentorSkill ms
            WHERE ms.mentorId = :mentorId
            """)
    void deleteByMentorId(
            @Param("mentorId") Long mentorId
    );
}
package com.cllg.chat_service.repository;

import com.cllg.chat_service.entity.Conversation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByStudentIdAndMentorId(
            Long studentId,
            Long mentorId
    );

    List<Conversation>
    findAllByStudentIdOrderByLastMessageAtDesc(
            Long studentId
    );

    List<Conversation>
    findAllByMentorIdOrderByLastMessageAtDesc(
            Long mentorId
    );


}
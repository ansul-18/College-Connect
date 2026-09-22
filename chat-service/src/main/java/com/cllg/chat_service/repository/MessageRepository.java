package com.cllg.chat_service.repository;

import com.cllg.chat_service.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository
        extends JpaRepository<Message, Long> {

    List<Message> findAllByConversationIdOrderByCreatedAtAsc(
            Long conversationId
    );

    List<Message> findAllByConversationIdAndReadFalse(
            Long conversationId
    );

    Optional<Message>
    findTopByConversationIdOrderByCreatedAtDesc(
            Long conversationId
    );
}
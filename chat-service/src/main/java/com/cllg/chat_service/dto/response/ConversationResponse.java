package com.cllg.chat_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {

    private Long id;

    private Long studentId;

    private Long mentorId;

    private LocalDateTime createdAt;

    private LocalDateTime lastMessageAt;

    private String lastMessage;
    private String lastMessageType;
    private long unreadCount;
}
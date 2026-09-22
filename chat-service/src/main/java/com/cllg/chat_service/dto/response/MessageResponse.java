package com.cllg.chat_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Long id;

    private Long conversationId;

    private Long senderId;

    private String senderRole;

    private String content;

    private String messageType;

    private String fileUrl;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private LocalDateTime createdAt;

    private Boolean read;
}
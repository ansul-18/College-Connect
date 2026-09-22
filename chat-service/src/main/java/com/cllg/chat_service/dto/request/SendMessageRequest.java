package com.cllg.chat_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendMessageRequest {

    @NotNull(
            message = "Conversation ID is required"
    )
    private Long conversationId;

    @NotBlank(
            message = "Message cannot be empty"
    )
    private String content;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(
            Long conversationId
    ) {
        this.conversationId =
                conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(
            String content
    ) {
        this.content =
                content;
    }
}
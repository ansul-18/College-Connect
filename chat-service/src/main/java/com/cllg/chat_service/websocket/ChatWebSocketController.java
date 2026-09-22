package com.cllg.chat_service.websocket;

import com.cllg.chat_service.dto.request.SendMessageRequest;
import com.cllg.chat_service.dto.response.MessageResponse;
import com.cllg.chat_service.entity.Conversation;
import com.cllg.chat_service.entity.Message;
import com.cllg.chat_service.service.ChatService;

import jakarta.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    private final SimpMessagingTemplate
            messagingTemplate;

    public ChatWebSocketController(
            ChatService chatService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatService =
                chatService;

        this.messagingTemplate =
                messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Valid SendMessageRequest request,
            SimpMessageHeaderAccessor accessor
    ) {

        /*
         * Get authenticated user
         */
        Map<String, Object> session =
                accessor.getSessionAttributes();

        if (session == null) {

            throw new IllegalArgumentException(
                    "WebSocket session not found"
            );
        }

        Long userId =
                (Long) session.get(
                        "userId"
                );

        String role =
                (String) session.get(
                        "role"
                );

        if (userId == null ||
                role == null) {

            throw new IllegalArgumentException(
                    "Unauthenticated WebSocket user"
            );
        }

        /*
         * Find conversation + verify
         * participant.
         */
        Conversation conversation =
                chatService.getConversation(
                        request.getConversationId(),
                        userId,
                        role
                );
        System.out.println("========== MESSAGE RECEIVED ==========");
        System.out.println("CONVERSATION ID = " + request.getConversationId());
        System.out.println("CONTENT = " + request.getContent());
        System.out.println("USER ID = " + userId);
        System.out.println("ROLE = " + role);
        /*
         * Save message
         */
        Message saved =
                chatService.saveMessage(
                        conversation,
                        userId,
                        role,
                        request.getContent()
                );
        System.out.println(
                "MESSAGE SAVED ID = " + saved.getId()
        );
        /*
         * Convert entity -> response
         */
        MessageResponse response =
                new MessageResponse(
                        saved.getId(),
                        saved.getConversationId(),
                        saved.getSenderId(),
                        saved.getSenderRole(),
                        saved.getContent(),
                        saved.getMessageType(),
                        saved.getFileUrl(),
                        saved.getFileName(),
                        saved.getFileType(),
                        saved.getFileSize(),
                        saved.getCreatedAt(),
                        saved.isRead()
                );

        /*
         * Broadcast message
         */
        messagingTemplate.convertAndSend(
                "/topic/conversation/"
                        + conversation.getId(),
                response
        );
    }
}
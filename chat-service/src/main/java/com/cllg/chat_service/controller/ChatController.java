package com.cllg.chat_service.controller;

import com.cllg.chat_service.dto.request.AuthenticatedUser;
import com.cllg.chat_service.dto.request.CreateConversationRequest;
import com.cllg.chat_service.dto.response.ConversationResponse;
import com.cllg.chat_service.dto.response.MessageResponse;
import com.cllg.chat_service.entity.Conversation;
import com.cllg.chat_service.entity.Message;
import com.cllg.chat_service.service.ChatService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.cllg.chat_service.service.FileStorageService;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final FileStorageService fileStorageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(
            ChatService chatService,
            FileStorageService fileStorageService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatService = chatService;
        this.fileStorageService = fileStorageService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationResponse>
    createConversation(
            @Valid
            @RequestBody
            CreateConversationRequest request,

            Authentication authentication
    ) {
        System.out.println("========== CHAT CREATE CALLED ==========");

        AuthenticatedUser user =
                getUser(authentication);
        System.out.println(
                "USER ID = " + user.userId()
                        + ", ROLE = " + user.role()
                        + ", MENTOR ID = " + request.getMentorId()
        );
        Conversation conversation =
                chatService.createOrGetConversation(
                        user.userId(),
                        request.getMentorId()
                );



        return ResponseEntity.ok(
                toConversationResponse(
                        conversation,
                        user.userId()
                )
        );

    }

    @GetMapping("/conversations/student")
    public ResponseEntity<List<ConversationResponse>>
    getStudentConversations(
            Authentication authentication
    ) {

        AuthenticatedUser user =
                getUser(authentication);

        return ResponseEntity.ok(
                chatService
                        .getStudentConversations(
                                user.userId()
                        )
                        .stream()
                        .map(conversation ->
                                toConversationResponse(
                                        conversation,
                                        user.userId()
                                )
                        )
                        .toList()
        );
    }

    @GetMapping("/conversations/mentor")
    public ResponseEntity<List<ConversationResponse>>
    getMentorConversations(
            Authentication authentication
    ) {

        AuthenticatedUser user =
                getUser(authentication);

        return ResponseEntity.ok(
                chatService
                        .getMentorConversations(
                                user.userId()
                        )
                        .stream()
                        .map(conversation ->
                                toConversationResponse(
                                        conversation,
                                        user.userId()
                                )
                        )
                        .toList()
        );
    }

    @GetMapping(
            "/conversations/{conversationId}"
    )
    public ResponseEntity<ConversationResponse>
    getConversation(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {

        AuthenticatedUser user =
                getUser(authentication);

        Conversation conversation =
                chatService.getConversation(
                        conversationId,
                        user.userId(),
                        user.role()
                );

        return ResponseEntity.ok(
                toConversationResponse(
                        conversation,
                        user.userId()
                )
        );
    }

    @GetMapping(
            "/conversations/{conversationId}/messages"
    )
    public ResponseEntity<List<MessageResponse>>
    getMessages(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {

        AuthenticatedUser user =
                getUser(authentication);

        return ResponseEntity.ok(
                chatService
                        .getMessages(
                                conversationId,
                                user.userId(),
                                user.role()
                        )
                        .stream()
                        .map(
                                this::toMessageResponse
                        )
                        .toList()
        );
    }

    @PatchMapping(
            "/conversations/{conversationId}/read"
    )
    public ResponseEntity<Void>
    markAsRead(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {

        AuthenticatedUser user =
                getUser(authentication);

        chatService.markConversationAsRead(
                conversationId,
                user.userId(),
                user.role()
        );

        return ResponseEntity.noContent()
                .build();
    }

    private AuthenticatedUser getUser(
            Authentication authentication
    ) {

        Long userId =
                Long.valueOf(
                        authentication.getName()
                );

        String role =
                authentication
                        .getAuthorities()
                        .stream()
                        .findFirst()
                        .map(
                                authority ->
                                        authority
                                                .getAuthority()
                                                .replace(
                                                        "ROLE_",
                                                        ""
                                                )
                        )
                        .orElse("UNKNOWN");

        return new AuthenticatedUser(
                userId,
                role
        );
    }

    @PostMapping(
            value = "/conversations/{conversationId}/attachments",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<MessageResponse> uploadAttachment(
            @PathVariable Long conversationId,

            @RequestPart("file")
            MultipartFile file,

            @RequestPart(
                    value = "caption",
                    required = false
            )
            String caption,

            Authentication authentication
    ) {

        Long userId =
                Long.valueOf(
                        authentication.getName()
                );

        String role =
                authentication
                        .getAuthorities()
                        .stream()
                        .findFirst()
                        .map(
                                authority ->
                                        authority
                                                .getAuthority()
                                                .replace(
                                                        "ROLE_",
                                                        ""
                                                )
                        )
                        .orElse("UNKNOWN");

        Message message =
                chatService.saveAttachment(
                        conversationId,
                        userId,
                        role,
                        file,
                        caption,
                        fileStorageService
                );

        MessageResponse response =
                toMessageResponse(message);

        /*
         * Broadcast attachment to everyone
         * subscribed to this conversation.
         */
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + conversationId,
                response
        );

        return ResponseEntity.ok(response);
    }

    private ConversationResponse toConversationResponse(
            Conversation conversation,
            Long currentUserId
    ) {

        Message lastMessage =
                chatService.getLastMessage(
                        conversation.getId()
                );

        long unreadCount =
                chatService.getUnreadCount(
                        conversation.getId(),
                        currentUserId
                );

        String lastMessageText = null;

        String lastMessageType = null;

        if (lastMessage != null) {

            lastMessageType =
                    lastMessage.getMessageType();

            if (lastMessage.getContent() != null &&
                    !lastMessage.getContent().isBlank()) {

                lastMessageText =
                        lastMessage.getContent();

            } else if ("IMAGE".equals(
                    lastMessage.getMessageType())) {

                lastMessageText =
                        "📷 Image";

            } else if ("DOCUMENT".equals(
                    lastMessage.getMessageType())) {

                lastMessageText =
                        "📄 " +
                                (lastMessage.getFileName() != null
                                        ? lastMessage.getFileName()
                                        : "Document");

            } else {

                lastMessageText =
                        "Message";
            }
        }

        return ConversationResponse.builder()
                .id(conversation.getId())
                .studentId(conversation.getStudentId())
                .mentorId(conversation.getMentorId())
                .createdAt(conversation.getCreatedAt())
                .lastMessageAt(conversation.getLastMessageAt())
                .lastMessage(lastMessageText)
                .lastMessageType(lastMessageType)
                .unreadCount(unreadCount)
                .build();
    }

    private MessageResponse
    toMessageResponse(
            Message message
    ) {

        return new MessageResponse(
                message.getId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getSenderRole(),
                message.getContent(),
                message.getMessageType(),
                message.getFileUrl(),
                message.getFileName(),
                message.getFileType(),
                message.getFileSize(),
                message.getCreatedAt(),
                message.isRead()
        );
    }
}
package com.cllg.chat_service.service;

import com.cllg.chat_service.client.MentorClient;
import com.cllg.chat_service.entity.Conversation;
import com.cllg.chat_service.entity.Message;
import com.cllg.chat_service.exception.ResourceNotFoundException;
import com.cllg.chat_service.repository.ConversationRepository;
import com.cllg.chat_service.repository.MessageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ChatAuthorizationService authorizationService;
    private final MentorClient mentorClient;

    public ChatService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            ChatAuthorizationService authorizationService,
            MentorClient mentorClient
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.authorizationService = authorizationService;
        this.mentorClient = mentorClient;
    }

    @Transactional
    public Conversation createOrGetConversation(
            Long studentId,
            Long mentorId
    ) {

        authorizationService.canStartChat(
                studentId,
                mentorId
        );

        return conversationRepository
                .findByStudentIdAndMentorId(
                        studentId,
                        mentorId
                )
                .orElseGet(() -> {

                    Conversation conversation =
                            Conversation.builder()
                                    .studentId(studentId)
                                    .mentorId(mentorId)
                                    .build();

                    return conversationRepository.save(conversation);
                });
    }

    @Transactional(readOnly = true)
    public Conversation getConversation(
            Long conversationId,
            Long userId,
            String role
    ) {

        Conversation conversation =
                conversationRepository
                        .findById(conversationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conversation not found"
                                )
                        );

        authorizationService.verifyParticipant(
                userId,
                role,
                conversation
        );

        return conversation;
    }

    @Transactional(readOnly = true)
    public List<Conversation> getStudentConversations(
            Long studentId
    ) {

        return conversationRepository
                .findAllByStudentIdOrderByLastMessageAtDesc(
                        studentId
                );
    }

    @Transactional(readOnly = true)
    public List<Conversation> getMentorConversations(
            Long mentorUserId
    ) {

        /*
         * JWT contains User ID.
         * Conversation stores MentorProfile ID.
         *
         * Example:
         * User ID = 20
         * MentorProfile ID = 5
         *
         * So first convert:
         * User ID -> MentorProfile ID
         */

        Long mentorProfileId =
                mentorClient.getMentorProfileIdByUserId(
                        mentorUserId
                );

        return conversationRepository
                .findAllByMentorIdOrderByLastMessageAtDesc(
                        mentorProfileId
                );
    }

    @Transactional(readOnly = true)
    public List<Message> getMessages(
            Long conversationId,
            Long userId,
            String role
    ) {

        getConversation(
                conversationId,
                userId,
                role
        );

        return messageRepository
                .findAllByConversationIdOrderByCreatedAtAsc(
                        conversationId
                );
    }

    @Transactional
    public Message saveMessage(
            Conversation conversation,
            Long senderId,
            String senderRole,
            String content
    ) {

        String cleanContent =
                content == null
                        ? ""
                        : content.trim();

        if (cleanContent.isBlank()) {

            throw new IllegalArgumentException(
                    "Message cannot be empty"
            );
        }

        Message message = new Message();

        message.setConversationId(
                conversation.getId()
        );

        message.setSenderId(
                senderId
        );

        message.setSenderRole(
                senderRole
        );

        message.setContent(
                cleanContent
        );


        message.setCreatedAt(
                LocalDateTime.now()
        );

        message.setRead(false);

        Message saved =
                messageRepository.save(message);

        conversation.setLastMessageAt(
                saved.getCreatedAt()
        );

        conversationRepository.save(conversation);

        return saved;
    }

    @Transactional
    public void markConversationAsRead(
            Long conversationId,
            Long userId,
            String role
    ) {

        getConversation(
                conversationId,
                userId,
                role
        );

        List<Message> messages =
                messageRepository
                        .findAllByConversationIdAndReadFalse(
                                conversationId
                        );

        for (Message message : messages) {

            if (!message.getSenderId().equals(userId)) {

                message.setRead(true);
            }
        }

        messageRepository.saveAll(messages);
    }

    public Message getLastMessage(Long conversationId) {

        return messageRepository
                .findTopByConversationIdOrderByCreatedAtDesc(
                        conversationId
                )
                .orElse(null);
    }

    public long getUnreadCount(
            Long conversationId,
            Long userId
    ) {

        return messageRepository
                .findAllByConversationIdAndReadFalse(
                        conversationId
                )
                .stream()
                .filter(message ->
                        !message.getSenderId().equals(userId)
                )
                .count();
    }

    @Transactional
    public Message saveAttachment(
            Long conversationId,
            Long senderId,
            String senderRole,
            MultipartFile file,
            String caption,
            FileStorageService fileStorageService
    ) {

        Conversation conversation =
                getConversation(
                        conversationId,
                        senderId,
                        senderRole
                );

        FileStorageService.StoredFile stored =
                fileStorageService.storeFile(file);

        String messageType =
                stored.contentType()
                        .startsWith("image/")
                        ? "IMAGE"
                        : "DOCUMENT";

        String cleanCaption =
                caption == null
                        ? ""
                        : caption.trim();

        Message message =
                Message.builder()
                        .conversationId(
                                conversation.getId()
                        )
                        .senderId(
                                senderId
                        )
                        .senderRole(
                                senderRole
                        )
                        .content(
                                cleanCaption
                        )
                        .messageType(
                                messageType
                        )
                        .fileUrl(
                                "/uploads/chat/"
                                        + stored.fileName()
                        )
                        .fileName(
                                stored.originalFileName()
                        )
                        .fileType(
                                stored.contentType()
                        )
                        .fileSize(
                                stored.size()
                        )
                        .read(false)
                        .build();

        Message saved =
                messageRepository.save(
                        message
                );

        conversation.setLastMessageAt(
                saved.getCreatedAt()
        );

        conversationRepository.save(
                conversation
        );

        return saved;
    }
}
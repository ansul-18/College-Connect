//package com.cllg.chat_service.service;
//
//import com.cllg.chat_service.dto.request.ChatMessageRequest;
//import com.cllg.chat_service.dto.response.MessageResponse;
//import com.cllg.chat_service.entity.Conversation;
//import com.cllg.chat_service.entity.Message;
//import com.cllg.chat_service.exception.ResourceNotFoundException;
//import com.cllg.chat_service.exception.UnauthorizedChatException;
//import com.cllg.chat_service.repository.ConversationRepository;
//import com.cllg.chat_service.repository.MessageRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class MessageServiceImpl implements MessageService {
//
//    private final MessageRepository messageRepository;
//    private final ConversationRepository conversationRepository;
//
//    @Override
//    public MessageResponse sendMessage(
//            ChatMessageRequest request,
//            Long senderId,
//            String senderRole) {
//
//        Conversation conversation = conversationRepository
//                .findById(request.getConversationId())
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Conversation not found: "
//                                        + request.getConversationId()
//                        )
//                );
//
//        validateSender(conversation, senderId, senderRole);
//
//        Message message = Message.builder()
//                .conversationId(conversation.getId())
//                .senderId(senderId)
//                .senderRole(senderRole)
//                .content(request.getContent().trim())
//                .read(false)
//                .build();
//
//        Message saved = messageRepository.save(message);
//
//        conversation.setLastMessageAt(LocalDateTime.now());
//        conversationRepository.save(conversation);
//
//        return map(saved);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MessageResponse> getConversationMessages(
//            Long conversationId) {
//
//        if (!conversationRepository.existsById(conversationId)) {
//            throw new ResourceNotFoundException(
//                    "Conversation not found: " + conversationId
//            );
//        }
//
//        return messageRepository
//                .findAllByConversationIdOrderByCreatedAtAsc(conversationId)
//                .stream()
//                .map(this::map)
//                .toList();
//    }
//
//    @Override
//    public void markConversationAsRead(Long conversationId) {
//
//        List<Message> messages = messageRepository
//                .findAllByConversationIdAndReadFalse(conversationId);
//
//        messages.forEach(message -> message.setRead(true));
//
//        messageRepository.saveAll(messages);
//    }
//
//    private void validateSender(
//            Conversation conversation,
//            Long senderId,
//            String senderRole) {
//
//        boolean student =
//                "ROLE_STUDENT".equals(senderRole)
//                        && conversation.getStudentId().equals(senderId);
//
//        boolean mentor =
//                "ROLE_MENTOR".equals(senderRole)
//                        && conversation.getMentorId().equals(senderId);
//
//        if (!student && !mentor) {
//            throw new UnauthorizedChatException(
//                    "You are not a participant in this conversation"
//            );
//        }
//    }
//
//    private MessageResponse map(Message message) {
//
//        return MessageResponse
//                .builder()
//                .id(message.getId())
//                .conversationId(message.getConversationId())
//                .senderId(message.getSenderId())
//                .senderRole(message.getSenderRole())
//                .content(message.getContent())
//                .createdAt(message.getCreatedAt())
//                .Read(message.isRead())
//                .build();
//    }
//}
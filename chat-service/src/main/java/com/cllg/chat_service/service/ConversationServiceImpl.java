//package com.cllg.chat_service.service;
//
//import com.cllg.chat_service.client.MentorClient;
//import com.cllg.chat_service.client.UserClient;
//import com.cllg.chat_service.dto.request.ConversationRequest;
//import com.cllg.chat_service.dto.response.ConversationResponse;
//import com.cllg.chat_service.dto.response.UserResponse;
//import com.cllg.chat_service.entity.Conversation;
//import com.cllg.chat_service.exception.ResourceNotFoundException;
//import com.cllg.chat_service.exception.UnauthorizedChatException;
//import com.cllg.chat_service.repository.ConversationRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//public class ConversationServiceImpl implements ConversationService {
//
//    private final ConversationRepository conversationRepository;
//    private final UserClient userClient;
//    private final MentorClient mentorClient;
//
//    public ConversationServiceImpl(
//            ConversationRepository conversationRepository,
//            UserClient userClient,
//            MentorClient mentorClient) {
//
//        this.conversationRepository = conversationRepository;
//        this.userClient = userClient;
//        this.mentorClient = mentorClient;
//    }
//
//    @Override
//    public ConversationResponse createOrGet(
//            ConversationRequest request) {
//
//        validateStudent(request.getStudentId());
//
//        Boolean canChat = mentorClient.hasChatAccess(
//                request.getStudentId(),
//                request.getMentorId()
//        );
//
//        if (!Boolean.TRUE.equals(canChat)) {
//            throw new UnauthorizedChatException(
//                    "Student does not have access to this mentor"
//            );
//        }
//
//        Conversation conversation =
//                conversationRepository
//                        .findByStudentIdAndMentorId(
//                                request.getStudentId(),
//                                request.getMentorId()
//                        )
//                        .orElseGet(() ->
//                                conversationRepository.save(
//                                        Conversation.builder()
//                                                .studentId(
//                                                        request.getStudentId()
//                                                )
//                                                .mentorId(
//                                                        request.getMentorId()
//                                                )
//                                                .build()
//                                )
//                        );
//
//        return mapToResponse(conversation);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public ConversationResponse getById(Long id) {
//
//        Conversation conversation = findById(id);
//
//        return mapToResponse(conversation);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ConversationResponse> getStudentConversations(
//            Long studentId) {
//
//        return conversationRepository
//                .findAllByStudentIdOrderByLastMessageAtDesc(studentId)
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ConversationResponse> getMentorConversations(
//            Long mentorId) {
//
//        return conversationRepository
//                .findAllByMentorIdOrderByLastMessageAtDesc(mentorId)
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }
//
//    private Conversation findById(Long id) {
//
//        return conversationRepository
//                .findById(id)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Conversation not found: " + id
//                        )
//                );
//    }
//
//    private void validateStudent(Long studentId) {
//
//        try {
//
//            UserResponse user = userClient.getStudentById(studentId);
//
//            if (user == null) {
//                throw new ResourceNotFoundException(
//                        "Student not found: " + studentId
//                );
//            }
//
//        } catch (ResourceNotFoundException ex) {
//
//            throw ex;
//
//        } catch (Exception ex) {
//
//            throw new ResourceNotFoundException(
//                    "Unable to validate student: " + studentId
//            );
//        }
//    }
//
//    private ConversationResponse mapToResponse(
//            Conversation conversation) {
//
//        return ConversationResponse
//                .builder()
//                .id(conversation.getId())
//                .studentId(conversation.getStudentId())
//                .mentorId(conversation.getMentorId())
//                .createdAt(conversation.getCreatedAt())
//                .lastMessageAt(conversation.getLastMessageAt())
//                .build();
//    }
//}
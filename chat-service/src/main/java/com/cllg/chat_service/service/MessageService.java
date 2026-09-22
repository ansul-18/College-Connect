//package com.cllg.chat_service.service;
//
//import com.cllg.chat_service.dto.request.ChatMessageRequest;
//import com.cllg.chat_service.dto.response.MessageResponse;
//
//import java.util.List;
//
//public interface MessageService {
//
//    MessageResponse sendMessage(
//            ChatMessageRequest request,
//            Long senderId,
//            String senderRole
//    );
//
//    List<MessageResponse>
//    getConversationMessages(
//            Long conversationId
//    );
//
//    void markConversationAsRead(
//            Long conversationId
//    );
//}
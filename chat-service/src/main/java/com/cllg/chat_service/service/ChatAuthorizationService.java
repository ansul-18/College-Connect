package com.cllg.chat_service.service;

import com.cllg.chat_service.client.MentorClient;
import com.cllg.chat_service.entity.Conversation;
import com.cllg.chat_service.exception.ChatAccessDeniedException;

import org.springframework.stereotype.Service;

@Service
public class ChatAuthorizationService {

    private final MentorClient mentorClient;

    public ChatAuthorizationService(
            MentorClient mentorClient
    ) {
        this.mentorClient =
                mentorClient;
    }

    /*
     * Student starts chat.
     */
    public void canStartChat(
            Long studentId,
            Long mentorId
    ) {

        System.out.println(
                "CHAT AUTH studentId=" + studentId
                        + ", mentorId=" + mentorId
        );

        boolean allowed =
                mentorClient.hasChatAccess(
                        mentorId
                );

        System.out.println(
                "MENTOR ACCESS RESULT=" + allowed
        );

        if (!allowed) {

            throw new ChatAccessDeniedException(
                    "Student does not have chat access"
            );
        }
    }


    /*
     * Check conversation participant.
     */
    public void verifyParticipant(
            Long userId,
            String role,
            Conversation conversation
    ) {

        /*
         * STUDENT
         */
        if ("STUDENT".equalsIgnoreCase(role)) {

            if (!userId.equals(
                    conversation.getStudentId()
            )) {

                throw new ChatAccessDeniedException(
                        "You are not a participant of this conversation"
                );
            }

            return;
        }


        /*
         * MENTOR
         *
         * conversation.mentorId
         * is MentorProfile ID.
         *
         * JWT userId
         * is User ID.
         */
        if ("MENTOR".equalsIgnoreCase(role)) {

            Long mentorUserId =
                    mentorClient.getMentorUserId(
                            conversation.getMentorId()
                    );

            if (!userId.equals(
                    mentorUserId
            )) {

                throw new ChatAccessDeniedException(
                        "You are not a participant of this conversation"
                );
            }

            return;
        }


        throw new ChatAccessDeniedException(
                "Invalid chat role"
        );
    }
}
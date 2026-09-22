package com.cllg.chat_service.exception;

public class UnauthorizedChatException
        extends RuntimeException {

    public UnauthorizedChatException(
            String message) {

        super(message);
    }
}
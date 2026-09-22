package com.cllg.chat_service.dto.request;

public record AuthenticatedUser(
        Long userId,
        String role
) {
}
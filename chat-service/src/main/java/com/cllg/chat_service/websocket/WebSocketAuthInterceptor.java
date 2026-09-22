package com.cllg.chat_service.websocket;

import com.cllg.chat_service.dto.request.AuthenticatedUser;
import com.cllg.chat_service.security.JwtService;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketAuthInterceptor
        implements ChannelInterceptor {

    private final JwtService jwtService;

    public WebSocketAuthInterceptor(
            JwtService jwtService
    ) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor
                        .wrap(message);

        if (StompCommand.CONNECT.equals(
                accessor.getCommand()
        )) {

            String authorization =
                    accessor.getFirstNativeHeader(
                            "Authorization"
                    );

            if (authorization == null ||
                    !authorization.startsWith(
                            "Bearer "
                    )) {

                throw new IllegalArgumentException(
                        "Missing WebSocket JWT"
                );
            }

            String token =
                    authorization.substring(7);

            AuthenticatedUser user =
                    jwtService.authenticate(token);

            /*
             * Authentication object
             */
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.userId().toString(),
                            null,
                            Collections.emptyList()
                    );

            /*
             * Principal
             */
            accessor.setUser(
                    authentication
            );

            /*
             * Session data
             */
            accessor.getSessionAttributes()
                    .put(
                            "userId",
                            user.userId()
                    );

            accessor.getSessionAttributes()
                    .put(
                            "role",
                            user.role()
                    );
        }

        return message;
    }
}
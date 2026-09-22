package com.cllg.chat_service.websocket;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
        implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor
            authInterceptor;

    public WebSocketConfig(
            WebSocketAuthInterceptor authInterceptor
    ) {
        this.authInterceptor =
                authInterceptor;
    }

    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry registry
    ) {

        /*
         * Client subscribes to:
         *
         * /topic/conversation/{id}
         */
        registry.enableSimpleBroker(
                "/topic"
        );

        /*
         * Client sends to:
         *
         * /app/chat.send
         */
        registry.setApplicationDestinationPrefixes(
                "/app"
        );
    }

    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry
    ) {

        registry.addEndpoint(
                        "/ws"
                )
                .setAllowedOrigins(
                        "http://localhost:5173"
                );
    }

    @Override
    public void configureClientInboundChannel(
            ChannelRegistration registration
    ) {

        registration.interceptors(
                authInterceptor
        );
    }
}
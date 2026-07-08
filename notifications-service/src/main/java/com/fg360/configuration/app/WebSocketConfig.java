package com.fg360.configuration.app;

import com.fg360.service.implementation.NotificationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(
        WebSocketConfig.class
    );

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/all");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws-alerts")
            .setAllowedOriginPatterns("*")
            .withSockJS();

        //.setAllowedOrigins(url frontend)

        logger.info("Punto final WebSocket registrado: /ws-alerts");
    }

    @EventListener
    public void onApplicationEvent(SessionConnectedEvent event) {
        logger.info("Conexión WebSocket establecida");
    }

    @EventListener
    public void onApplicationEvent(SessionDisconnectEvent event) {
        logger.info("Conexión WebSocket desconectada");
    }
}

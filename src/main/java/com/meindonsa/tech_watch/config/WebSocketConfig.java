package com.meindonsa.tech_watch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${webapp.website}")
    private String webappUrl;

    @Value("${socket.prefix}")
    private String prefix;

    @Value("${socket.endPoint}")
    private String endPoint;

    @Value("${socket.broker}")
    private String broker;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endPoint)
                .setAllowedOrigins(webappUrl)
                .withSockJS()
                .setHeartbeatTime(15000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(broker);
        registry.setApplicationDestinationPrefixes(prefix);
    }
}

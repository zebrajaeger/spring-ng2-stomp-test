package de.zebrajaeger.stomptest;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // default naming for topics is 'topic', for user messages is 'queue'
        config.enableSimpleBroker("/topic/", "/queue/");

        // prefix for messaging. @MessageMapping("/from-client") means: we listen to "/app/from-client"
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // ws/stomp listening on localhost:8080/greeting
        registry.addEndpoint("/greeting").setAllowedOrigins("*");
    }
}

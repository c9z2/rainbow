package com.milchstrabe.rainbow.ws.common.config;

import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.ws.common.Constant;
import com.milchstrabe.rainbow.ws.common.DefaultPrincipal;
import com.milchstrabe.rainbow.ws.interceptor.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rainbow-ws")
                .addInterceptors(new AuthHandshakeInterceptor())
                .setHandshakeHandler(new DefaultHandshakeHandler(){
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {

                        UCI uci = (UCI) attributes.get(Constant.USER_IN_SESSION);
                        return new DefaultPrincipal(uci);
                    }
                })
                .withSockJS();
    }



}
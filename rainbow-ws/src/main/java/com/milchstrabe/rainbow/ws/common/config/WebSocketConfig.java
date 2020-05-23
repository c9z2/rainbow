package com.milchstrabe.rainbow.ws.common.config;

import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.ws.common.Constant;
import com.milchstrabe.rainbow.ws.common.DefaultPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rainbow-ws")
                .addInterceptors(new HandshakeInterceptor(){
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        HttpSession session = getSession(request);
                        Object attribute = session.getAttribute(Constant.USER_IN_SESSION);
                        if(attribute == null){
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler(){
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        HttpSession session = getSession(request);
                        Object attribute = session.getAttribute(Constant.USER_IN_SESSION);
                        UCI uci = null;
                        if(attribute != null){
                            uci = (UCI) attribute;
                        }
                        return new DefaultPrincipal(uci);
                    }
                })
                .setAllowedOrigins("*")
                .withSockJS();
    }



    @Nullable
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest)request;
            return  serverRequest.getServletRequest().getSession(false);
        } else {
            return null;
        }
    }

}
package com.milchstrabe.rainbow.ws.typ3.stomp.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.ws.common.DefaultPrincipal;
import com.milchstrabe.rainbow.ws.repository.ClientServerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private ClientServerRepository clientServerRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rainbow-ws")
                .setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        log.info("...1");
                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            String token = servletRequest.getServletRequest().getParameter("sid");
                            String cid = servletRequest.getServletRequest().getParameter("cid");
                            if (token == null || cid == null) {
                                return false;
                            }
                            DecodedJWT decode = null;
                            try {
                                decode = JWT.decode(token);
                            } catch (JWTDecodeException exception) {
                                log.error(exception.getMessage() +"token:"+token);
                                return false;
                            }

                            String userId = decode.getClaim("userId").asString();
                            String username = decode.getClaim("username").asString();

                            attributes.put("userId", userId);
                            attributes.put("username", username);
                            attributes.put("cid", cid);
                        }
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        log.info("handl shake ok ... 3");
                        String username = attributes.get("username").toString();
                        String userId = attributes.get("userId").toString();
                        String cid = attributes.get("cid").toString();

                        //save grpc info to redis
                        ClientServer cs = ClientServer.builder()
                                .cid(cid)
                                .host("127.0.0.1")
                                .port(8083)
                                .cType("WEB")
                                .build();

                        clientServerRepository.addCS(cs,userId);

                        UCI uci = UCI.builder().username(username).uid(userId).cid(cid).build();
                        DefaultPrincipal principal = new DefaultPrincipal(uci);
                        return principal;
                    }
                })
                .withSockJS();
    }
}
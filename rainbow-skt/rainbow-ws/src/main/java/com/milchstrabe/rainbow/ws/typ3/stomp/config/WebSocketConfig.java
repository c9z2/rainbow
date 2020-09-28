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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
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

    @Value("${node.host}")
    private String node;

    @Value("${gRPC.port}")
    private Integer gRPCPort;
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
                        //TODO 判断多设备登入问题
                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

                        String username = attributes.get("username").toString();
                        String userId = attributes.get("userId").toString();
                        String cid = attributes.get("cid").toString();

                        //save grpc info to redis
                        ClientServer cs = ClientServer.builder()
                                .cid(cid)
                                .host(node)
                                .port(gRPCPort)
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

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        Map<String, Object> attributes = session.getAttributes();

                        String userId = attributes.get("userId").toString();
                        String cid = attributes.get("cid").toString();
                        ClientServer cs = ClientServer.builder()
                                .cid(cid)
                                .host(node)
                                .port(gRPCPort)
                                .cType("WEB")
                                .build();

                        clientServerRepository.removeCS(cs,userId);
                        log.info("clear client info");
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }



}
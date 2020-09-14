package com.milchstrabe.rainbow.udp.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.server.domain.ClientType;
import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.udp.common.constant.SessionKey;
import com.milchstrabe.rainbow.udp.repository.ClientServerRepository;
import com.milchstrabe.rainbow.udp.service.ISignInService;
import com.milchstrabe.rainbow.udp.typ3.netty.session.Session;
import com.milchstrabe.rainbow.udp.typ3.netty.session.SessionAttribute;
import com.milchstrabe.rainbow.udp.typ3.netty.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:57
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Service
public class SignInServiceImpl implements ISignInService {

    @Autowired
    private ClientServerRepository clientServerRepository;

    @Value("${node.host}")
    private String host;

    @Value("${netty.gRPC.port}")
    private int gRPCPort;


    @Override
    public void signIn(String token, String cid, ClientType cType, Session session) throws AuthException {
        DecodedJWT decode = null;
        try {
            decode = JWT.decode(token);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new AuthException(e.getMessage());
        }

        String userId = decode.getClaim("userId").asString();
        String username = decode.getClaim("username").asString();
        UCI uci = UCI.builder().cid(cid).uid(userId).username(username).build();

        //mark session
        SessionAttribute sessionAttribute = new SessionAttribute();
        sessionAttribute.put(SessionKey.CLIENT_IN_SESSION,uci);
        session.setAttachment(sessionAttribute);
        SessionManager.putSession(username,cid,session);

        //save grpc info to redis
        ClientServer cs = ClientServer.builder()
                .cid(cid)
                .host(host)
                .port(gRPCPort)
                .cType(cType.name())
                .build();

        clientServerRepository.addCS(cs,username);
    }
}

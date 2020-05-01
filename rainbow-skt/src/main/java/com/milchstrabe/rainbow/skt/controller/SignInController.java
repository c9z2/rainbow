package com.milchstrabe.rainbow.skt.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPRequest;
import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPResponse;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyMapping;
import com.milchstrabe.rainbow.skt.server.tcp.session.SessionAttribute;
import com.milchstrabe.rainbow.skt.service.ISignInService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
@NettyController(cmd = 0)
public class SignInController {

    @Autowired
    private ISignInService signInService;

    @NettyMapping(cmd = 0)
    public TCPResponse signIn(TCPRequest tcpRequest){

        Data.Request request = tcpRequest.getRequest();
        ByteString data = request.getData();
        String token = data.toStringUtf8();

        DecodedJWT decodedJWT = signInService.signIn(token);

        SessionAttribute sessionAttribute = new SessionAttribute();
        sessionAttribute.put("jwt",decodedJWT);
        tcpRequest.getSession().setAttachment(sessionAttribute);

        TCPResponse tcpResponse = TCPResponse.builder()
                .response(Data.Response
                        .newBuilder()
                        .setCmd1(request.getCmd1())
                        .setCmd2(request.getCmd2())
                        .setCode(2)
                        .build()
                )
                .build();
        return tcpResponse;
    }
}

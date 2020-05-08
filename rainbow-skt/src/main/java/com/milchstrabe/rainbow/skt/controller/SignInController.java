package com.milchstrabe.rainbow.skt.controller;

import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.skt.common.constant.SessionKey;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.session.Request;
import com.milchstrabe.rainbow.skt.server.session.SessionAttribute;
import com.milchstrabe.rainbow.skt.server.session.SessionManager;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyMapping;
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
    public Data.Response signIn(Request request){

        Data.Request dataRequest = request.getRequest();
        ByteString data = dataRequest.getData();
        String token = data.toStringUtf8();

        User user = signInService.signIn(token);

        SessionAttribute sessionAttribute = new SessionAttribute();
        sessionAttribute.put(SessionKey.CLIENT_IN_SESSION,user);
        request.getSession().setAttachment(sessionAttribute);
        SessionManager.putSession(user.getUsername(),request.getSession());

        return Data.Response
                .newBuilder()
                .setCmd1(dataRequest.getCmd1())
                .setCmd2(dataRequest.getCmd2())
                .setCode(2)
                .build();
    }
}

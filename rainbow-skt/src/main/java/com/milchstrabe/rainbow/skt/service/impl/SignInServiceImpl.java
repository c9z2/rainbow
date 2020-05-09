package com.milchstrabe.rainbow.skt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.skt.server.ServerByCurator;
import com.milchstrabe.rainbow.skt.service.ISignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:57
 * @Version 1.0
 * @Description
 **/
@Service
public class SignInServiceImpl implements ISignInService {

    @Autowired
    private ServerByCurator serverByCurator;

    @Override
    public User signIn(String token) throws Exception {
        DecodedJWT decode = JWT.decode(token);
        String userId = decode.getClaim("userId").asString();
        String username = decode.getClaim("username").asString();
        User user = User.builder()
                .username(username)
                .id(userId)
                .build();

        serverByCurator.updateData2Node();

        return user;
    }
}

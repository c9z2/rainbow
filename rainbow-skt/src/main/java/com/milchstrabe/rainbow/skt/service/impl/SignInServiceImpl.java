package com.milchstrabe.rainbow.skt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.skt.service.ISignInService;
import org.springframework.stereotype.Service;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:57
 * @Version 1.0
 * @Description
 **/
@Service
public class SignInServiceImpl implements ISignInService {

    @Override
    public DecodedJWT signIn(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode;
    }
}

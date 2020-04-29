package com.milchstrabe.rainbow.skt.service;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface ISignInService {

    DecodedJWT signIn(String token);
}

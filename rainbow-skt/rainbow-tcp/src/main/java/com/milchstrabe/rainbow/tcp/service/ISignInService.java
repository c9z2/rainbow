package com.milchstrabe.rainbow.tcp.service;

import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.server.domain.ClientType;
import com.milchstrabe.rainbow.tcp.typ3.netty.session.Session;

public interface ISignInService {

    void signIn(String token, String cid, ClientType cType, Session session) throws AuthException;
}

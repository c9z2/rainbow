package com.milchstrabe.rainbow.skt.service;

import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.server.domain.ClientType;
import com.milchstrabe.rainbow.skt.server.session.Session;

public interface ISignInService {

    void signIn(String token, String cid, ClientType cType, Session session) throws AuthException;
}

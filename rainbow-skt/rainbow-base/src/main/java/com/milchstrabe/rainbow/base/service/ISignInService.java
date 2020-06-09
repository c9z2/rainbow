package com.milchstrabe.rainbow.base.service;

import com.milchstrabe.rainbow.base.server.session.Session;
import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.server.domain.ClientType;

public interface ISignInService {

    void signIn(String token, String cid, ClientType cType, Session session) throws AuthException;
}

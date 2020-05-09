package com.milchstrabe.rainbow.skt.service;

import com.milchstrabe.rainbow.biz.domain.po.User;

public interface ISignInService {

    User signIn(String token) throws Exception;
}

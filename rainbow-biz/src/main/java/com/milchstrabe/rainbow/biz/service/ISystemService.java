package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.exception.LogicException;

public interface ISystemService {

    String signIn(String username, String password) throws LogicException;

    String fingerprint(User user, String ctype) throws LogicException;
}

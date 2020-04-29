package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.exception.LogicException;

public interface ISystemService {

    String signIn(String username, String password) throws LogicException;
}

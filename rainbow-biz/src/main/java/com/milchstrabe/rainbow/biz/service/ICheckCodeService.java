package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.exception.LogicException;

/**
 * @Author ch3ng
 * @Date 2020/9/3 13:05
 * @Version 1.0
 * @Description
 **/
public interface ICheckCodeService {

    void sendSignUpCheckcode(String email) throws LogicException;

    void sendResetCheckCode(String username,String email) throws LogicException;
}

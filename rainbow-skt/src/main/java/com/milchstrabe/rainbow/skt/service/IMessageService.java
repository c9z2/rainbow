package com.milchstrabe.rainbow.skt.service;

import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.skt.server.codc.Data;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:43
 * @Version 1.0
 * @Description
 **/
public interface IMessageService {

    boolean doMessage(Data.Request request) throws LogicException;
}

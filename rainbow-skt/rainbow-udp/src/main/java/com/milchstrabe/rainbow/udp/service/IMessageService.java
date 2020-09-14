package com.milchstrabe.rainbow.udp.service;

import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.exception.LogicException;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:43
 * @Version 1.0
 * @Description
 **/
public interface IMessageService {

    boolean doMessage(Data.Request request) throws LogicException;
}

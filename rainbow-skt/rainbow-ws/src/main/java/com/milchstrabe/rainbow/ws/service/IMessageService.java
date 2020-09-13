package com.milchstrabe.rainbow.ws.service;

import com.milchstrabe.rainbow.server.domain.po.Message;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:43
 * @Version 1.0
 * @Description
 **/
public interface IMessageService<T> {

    void doMessage(Message<T> request);
}

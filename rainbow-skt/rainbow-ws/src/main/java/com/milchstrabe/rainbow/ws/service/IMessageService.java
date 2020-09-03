package com.milchstrabe.rainbow.ws.service;

import com.milchstrabe.rainbow.server.domain.dto.Message;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:43
 * @Version 1.0
 * @Description
 **/
public interface IMessageService {

    void doMessage(Message request);
}

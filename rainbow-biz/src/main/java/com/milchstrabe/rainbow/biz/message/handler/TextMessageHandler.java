package com.milchstrabe.rainbow.biz.message.handler;

import com.milchstrabe.rainbow.biz.message.MessageHandler;
import com.milchstrabe.rainbow.server.domain.po.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:44
 * @Version 1.0
 * @Description
 **/
@MessageHandler(type = 1)
@Slf4j
public class TextMessageHandler implements IMessageHandler<String> {


    @Override
    public void handle(Message<String> request) {

    }
}

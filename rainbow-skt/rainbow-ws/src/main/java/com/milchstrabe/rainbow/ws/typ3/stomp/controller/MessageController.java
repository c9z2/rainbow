package com.milchstrabe.rainbow.ws.typ3.stomp.controller;

import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import com.milchstrabe.rainbow.server.domain.po.Message;
import com.milchstrabe.rainbow.ws.service.IMessageService;
import com.milchstrabe.rainbow.ws.typ3.stomp.handler.MessageProcessorContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class MessageController {

    /**
     *      string msgId = 1;
     *      int32 msgType = 2;
     *      string content = 3;
     *      string sender = 4;
     *      string receiver = 5;
     *      string date = 6;
     * @param messageStr
     * @return
     */
    @MessageMapping("/message")
    @SendToUser("/message")
    public String msg(String messageStr) {
        log.info("msg:[{}]",messageStr);
        Gson gson = new Gson();
        Message message = gson.fromJson(messageStr, Message.class);
        Integer msgType = message.getMsgType();
        message.setDate(System.currentTimeMillis());
        message.setId(IdUtil.objectId());

        IMessageService messageService = MessageProcessorContainer.get(msgType);
        messageService.doMessage(message);

        String json = gson.toJson(message);
        return json;
    }


}

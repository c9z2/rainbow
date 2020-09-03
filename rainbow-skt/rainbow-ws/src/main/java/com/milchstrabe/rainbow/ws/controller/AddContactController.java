package com.milchstrabe.rainbow.ws.controller;

import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import com.milchstrabe.rainbow.ws.domain.vo.MessageResponse;
import com.milchstrabe.rainbow.ws.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class AddContactController {


    @Autowired
    private IMessageService messageService;


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
    @MessageMapping("/c")
    @SendToUser("/message")
    public String msg(String messageStr) {
        log.debug("msg:[{}]",messageStr);
        Gson gson = new Gson();
        Message message = gson.fromJson(messageStr, Message.class);
        message.setDate(System.currentTimeMillis());
        String msgId = IdUtil.objectId();
        message.setId(msgId);
        messageService.doMessage(message);
        MessageResponse messageResponse = new MessageResponse();
        BeanUtils.copyProperties(message,messageResponse);
        String json = gson.toJson(messageResponse);
        return json;
    }


}

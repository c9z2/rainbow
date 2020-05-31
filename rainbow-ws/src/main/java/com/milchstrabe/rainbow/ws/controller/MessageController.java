package com.milchstrabe.rainbow.ws.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.ws.domain.dto.MessageRequest;
import com.milchstrabe.rainbow.ws.domain.vo.MessageResponse;
import com.milchstrabe.rainbow.ws.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
public class MessageController {


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
    @MessageMapping("/message")
    @SendToUser("/message")
    public String msg(String messageStr) {
        log.debug("msg:[{}]",messageStr);
        Gson gson = new Gson();
        MessageRequest messageRequest = gson.fromJson(messageStr, MessageRequest.class);
        messageRequest.setDate(System.currentTimeMillis());
        messageRequest.setId(UUID.randomUUID().toString().replace("-",""));
        messageService.doMessage(messageRequest);
        MessageResponse messageResponse = new MessageResponse();
        BeanUtils.copyProperties(messageRequest,messageResponse);
        BeanUtils.copyProperties(messageRequest,messageResponse);
        String json = gson.toJson(messageResponse);
        return json;
    }


}

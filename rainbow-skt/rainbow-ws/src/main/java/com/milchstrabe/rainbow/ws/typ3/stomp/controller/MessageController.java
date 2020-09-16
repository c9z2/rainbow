package com.milchstrabe.rainbow.ws.typ3.stomp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class MessageController {

    @MessageMapping("/message")
    @SendToUser("/message")
    public String msg(String messageStr) {
        log.info("msg:[{}]",messageStr);

        return messageStr;
    }


}

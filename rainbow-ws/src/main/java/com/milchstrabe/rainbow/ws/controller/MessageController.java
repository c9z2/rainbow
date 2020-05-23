package com.milchstrabe.rainbow.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/message")
    @SendToUser("/message")
    public String msg(String name) {
        System.out.println(name);
        simpMessageSendingOperations.convertAndSendToUser("49ba59abbe56e057", "/message", name);
        return name;
    }


}

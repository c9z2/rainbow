package com.milchstrabe.rainbow.ws.controller;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/msg")
    @SendToUser("/msg")
    public Data.Response msg(String msg) {

        return null;
    }

}

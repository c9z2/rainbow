package com.milchstrabe.rainbow.skt.controller;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.session.Request;
import com.milchstrabe.rainbow.skt.server.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.annotion.NettyMapping;
import com.milchstrabe.rainbow.skt.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
@NettyController(cmd = 1)
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @NettyMapping(cmd = 1)
    public Data.Response msg(Request request){
        Data.Request dataRequest = request.getRequest();
        boolean isSuccess = messageService.doMessage(dataRequest);
        int status = 5;
        if(isSuccess){
            status = 2;
        }

        return Data.Response.newBuilder()
                .setCmd1(dataRequest.getCmd1())
                .setCmd2(dataRequest.getCmd2())
                .setCode(status)
                .build();
    }
}

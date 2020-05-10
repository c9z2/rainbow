package com.milchstrabe.rainbow.skt.controller;

import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.skt.common.constant.StateCode;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.session.Request;
import com.milchstrabe.rainbow.skt.server.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.annotion.NettyMapping;
import com.milchstrabe.rainbow.skt.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
@Slf4j
@NettyController(cmd = 1)
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @NettyMapping(cmd = 1)
    public Data.Response msg(Request request){
        Data.Request dataRequest = request.getRequest();
        try {
            boolean isSuccess = messageService.doMessage(dataRequest);
            return Data.Response.newBuilder()
                    .setCmd1(dataRequest.getCmd1())
                    .setCmd2(dataRequest.getCmd2())
                    .setCode(StateCode.SUCCESS)
                    .build();
        } catch (LogicException e) {
            log.error("[{}]:{}",e.CODE,e.getMessage());
            return Data.Response.newBuilder()
                    .setCmd1(dataRequest.getCmd1())
                    .setCmd2(dataRequest.getCmd2())
                    .setCode(e.CODE)
                    .build();
        }
    }
}

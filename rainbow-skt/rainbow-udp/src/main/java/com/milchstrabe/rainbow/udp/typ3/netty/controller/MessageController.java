package com.milchstrabe.rainbow.udp.typ3.netty.controller;

import com.milchstrabe.rainbow.api.netty.codc.Data;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.udp.common.constant.StateCode;
import com.milchstrabe.rainbow.udp.service.IMessageService;
import com.milchstrabe.rainbow.udp.typ3.netty.annotion.NettyController;
import com.milchstrabe.rainbow.udp.typ3.netty.annotion.NettyMapping;
import com.milchstrabe.rainbow.udp.typ3.netty.session.Request;
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

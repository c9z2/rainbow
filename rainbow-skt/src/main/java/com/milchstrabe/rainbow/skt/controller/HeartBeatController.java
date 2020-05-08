package com.milchstrabe.rainbow.skt.controller;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyMapping;
import com.milchstrabe.rainbow.skt.server.udp.codc.UDPRequest;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 * cmd1:0 --> cmd2:1
 **/
@NettyController(cmd = 0)
public class HeartBeatController {

    @NettyMapping(cmd = 1)
    public Data.Response heartBeat(UDPRequest udpRequest){

        Data.Request request = udpRequest.getRequest();

        return Data.Response
                .newBuilder()
                .setCmd1(request.getCmd1())
                .setCmd2(request.getCmd2())
                .setCode(2)
                .build();
    }
}

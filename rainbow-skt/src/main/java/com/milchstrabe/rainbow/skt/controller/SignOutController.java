package com.milchstrabe.rainbow.skt.controller;

import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPRequest;
import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPResponse;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyMapping;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
//@NettyController(cmd = 1)
public class SignOutController {

    @NettyMapping(cmd = 1)
    public TCPResponse signIn(TCPRequest TCPRequest){
        return null;
    }
}

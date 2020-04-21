package com.milchstrabe.rainbow.skt.controller;

import com.milchstrabe.rainbow.skt.server.codc.Request;
import com.milchstrabe.rainbow.skt.server.codc.Response;
import com.milchstrabe.rainbow.skt.server.codc.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.codc.annotion.NettyMapping;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
@NettyController(cmd = 1)
public class SignInController {

    @NettyMapping(cmd = 1)
    public Response signIn(Request request){
        return null;
    }
}

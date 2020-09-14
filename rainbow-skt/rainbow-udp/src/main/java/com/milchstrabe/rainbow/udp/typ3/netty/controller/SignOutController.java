package com.milchstrabe.rainbow.udp.typ3.netty.controller;


import com.milchstrabe.rainbow.udp.typ3.netty.annotion.NettyMapping;
import com.milchstrabe.rainbow.udp.typ3.netty.session.Request;
import com.milchstrabe.rainbow.udp.typ3.netty.session.Response;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
//@NettyController(cmd = 1)
public class SignOutController {

    @NettyMapping(cmd = 1)
    public Response signIn(Request request){
        return null;
    }
}

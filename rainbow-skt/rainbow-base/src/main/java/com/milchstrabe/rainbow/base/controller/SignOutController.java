package com.milchstrabe.rainbow.base.controller;


import com.milchstrabe.rainbow.base.server.annotion.NettyMapping;
import com.milchstrabe.rainbow.base.server.session.Request;
import com.milchstrabe.rainbow.base.server.session.Response;

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

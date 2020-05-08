package com.milchstrabe.rainbow.cli.controller;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.channel.Channel;

/**
 * @Author ch3ng
 * @Date 2020/5/8 14:44
 * @Version 1.0
 * @Description
 **/
public abstract class AbstractController {

    public abstract void resp(Data.Response response, Channel channel);

}

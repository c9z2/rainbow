package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.cli.CLI;
import com.milchstrabe.rainbow.cli.controller.Invoker;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoTCPClientHandler extends SimpleChannelInboundHandler<Data.Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Data.Response resp) throws Exception {
        int cmd1 = resp.getCmd1();
        int cmd2 = resp.getCmd2();
        Invoker invoker = CLI.controllerContext.getInvoker(cmd1, cmd2);
        if(invoker != null){
            Object invoke = invoker.invoke(resp,ctx.channel());
            System.out.print("$ ");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection ...");
    }
}
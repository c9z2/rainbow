package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoUDPClientHandler extends SimpleChannelInboundHandler<Data.Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Data.Response resp) throws Exception {
        com.google.protobuf.ByteString data = resp.getData();
        String msg = data.toStringUtf8();
        int code = resp.getCode();

        System.out.println("code:"+code);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // open datagram channel
        System.out.println();
        super.channelInactive(ctx);
    }

}
package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.client.codc.Data;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoClientHandler extends SimpleChannelInboundHandler<Data.Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Data.Response resp) throws Exception {
        com.google.protobuf.ByteString data = resp.getData();
        String msg = data.toStringUtf8();
        System.out.println(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        DataInfo.RequestUser user = DataInfo.RequestUser.newBuilder()
//                .setUserName("zhihao.miao").setAge(27).setPassword("123456").build();
//        ctx.channel().writeAndFlush(user);
    }
}
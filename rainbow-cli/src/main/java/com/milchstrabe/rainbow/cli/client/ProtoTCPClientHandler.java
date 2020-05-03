package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class ProtoTCPClientHandler extends SimpleChannelInboundHandler<Data.Response> {

    private Timer timer = new Timer();

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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Data.Request request = Data.Request.newBuilder()
                        .setCmd1(0)
                        .setCmd2(1)
                        .build();


                ByteBuf dataBuf = Unpooled.copiedBuffer(request.toByteArray());
                DatagramPacket packet = new DatagramPacket(dataBuf, new InetSocketAddress("localhost",8082));
                UDPClient.channel.writeAndFlush(packet);
            }
        },0,2000);

        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection ...");
//        DataInfo.RequestUser user = DataInfo.RequestUser.newBuilder()
//                .setUserName("zhihao.miao").setAge(27).setPassword("123456").build();
//        ctx.channel().writeAndFlush(user);
    }
}
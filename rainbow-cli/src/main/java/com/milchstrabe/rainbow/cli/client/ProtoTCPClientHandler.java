package com.milchstrabe.rainbow.cli.client;

import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.grpc.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class ProtoTCPClientHandler extends SimpleChannelInboundHandler<Data.Response> {

    private Timer timer = new Timer();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Data.Response resp) throws Exception {
        ByteString data = resp.getData();
        if(resp.getCmd1() == 1 && resp.getCmd2() == 1){
            Msg.MsgRequest msgRequest = Msg.MsgRequest.parseFrom(data);
            System.out.println(msgRequest);
        }
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
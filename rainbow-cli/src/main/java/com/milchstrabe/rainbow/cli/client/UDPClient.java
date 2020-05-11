package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.DatagramPacketEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @Author ch3ng
 * @Date 2020/5/2 18:38
 * @Version 1.0
 * @Description
 **/
public class UDPClient {

    private Bootstrap bootstrap;
    private static NioEventLoopGroup workerGroup;
    public static Channel channel;
    public static ChannelFuture channelFuture;

    public void start(){
        try {
            bootstrap = new Bootstrap();
            workerGroup = new NioEventLoopGroup();
            bootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch)throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DatagramPacketDecoder(new ProtobufDecoder(Data.Response.getDefaultInstance())));
                            pipeline.addLast(new DatagramPacketEncoder<Data.Request>(new ProtobufEncoder()));
                            pipeline.addLast(new ProtoUDPClientHandler());
                        }
                    });
            channelFuture = bootstrap.bind(0).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void destory(){
        if(channel != null){
            channel.close();
        }
        workerGroup.shutdownGracefully();
    }

}

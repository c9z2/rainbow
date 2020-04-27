package com.milchstrabe.rainbow.skt.server.tcp;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author ch3ng
 * @Date 2020/4/20 20:25
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class NettyTCPServer {

    // 创建boss和worker
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventLoopGroup busyGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start(int port){
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置循环线程组事例
            serverBootstrap.group(bossGroup, workerGroup);

            // 设置channel工厂
            serverBootstrap.channel(NioServerSocketChannel.class);

            // 设置管道
            // 应需要设置在消息处理时使用多线程
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IdleStateHandler(0,0,20));
//                    ch.pipeline().addLast(new RequestDecoder());
//                    ch.pipeline().addLast(new ResponseEncoder());
//                    ch.pipeline().addLast(busyGroup,new ServerHandler());
                    //解码器，通过Google Protocol Buffers序列化框架动态的切割接收到的ByteBuf
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    //服务器端接收的是客户端RequestUser对象，所以这边将接收对象进行解码生产实列
                    ch.pipeline().addLast(new ProtobufDecoder(Data.Request.getDefaultInstance()));
                    //Google Protocol Buffers编码器
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    //Google Protocol Buffers编码器
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(busyGroup,new ProtoServerHandler());
                }
            });

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);// 链接缓冲池队列大小
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);//维持活跃的链接，清楚死链接
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY,true);//关闭延迟发送

            channelFuture = serverBootstrap.bind(port).sync();/*.channel().closeFuture().sync()*/
            channel = channelFuture.channel();
        } catch (Exception e) {
            log.error(e.getMessage());
        }finally {
            if (channelFuture != null && channelFuture.isSuccess()) {
                log.info("Netty started on port(s): {} (tcp)",port);
            } else {
                log.error("Netty started Error!");
            }
        }
        return channelFuture;
    }

    /**
     * shutdown service
     */
    public void destroy() {
        log.info("Shutdown Netty TCP Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty TCP Server Success!");
    }
}

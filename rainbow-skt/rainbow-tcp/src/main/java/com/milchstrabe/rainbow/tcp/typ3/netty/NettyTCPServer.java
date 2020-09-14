package com.milchstrabe.rainbow.tcp.typ3.netty;

import com.milchstrabe.rainbow.base.server.codc.Data;
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
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import io.netty.util.internal.SystemPropertyUtil;
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

    // 默认业务线程数
    private static final int DEFAULT_BUSY_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

    // 创建boss和worker
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("rainbow-skt-boosGroup"));
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(new DefaultThreadFactory("rainbow-skt-workGroup"));
    private final EventExecutorGroup busyGroup = new UnorderedThreadPoolEventExecutor(DEFAULT_BUSY_LOOP_THREADS, new DefaultThreadFactory("rainbow-skt-busyGroup"));

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
                    ch.pipeline().addLast(new IdleStateHandler(0,0,60));
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufDecoder(Data.Request.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
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
        bossGroup.shutdownGracefully();
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        log.info("Shutdown Netty TCP Server Success!");
    }
}

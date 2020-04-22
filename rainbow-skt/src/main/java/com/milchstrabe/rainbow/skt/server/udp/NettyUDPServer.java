package com.milchstrabe.rainbow.skt.server.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author ch3ng
 * @Date 2020/4/22 22:01
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class NettyUDPServer {

	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final EventLoopGroup busyGroup = new NioEventLoopGroup();
	private Channel channel;
 

	public ChannelFuture start(int port) {
		ChannelFuture channelFuture = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup).channel(NioDatagramChannel.class)
					.handler(new ChannelInitializer<NioDatagramChannel>() {
						@Override
						protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
							ChannelPipeline pipeline = nioDatagramChannel.pipeline();
							// 添加UDP解码器
//							 new ProtobufDecoder(Message.getDefaultInstance())));
							// 添加UDP编码器
//							 pipeline.addLast("datagramPacketEncoder", new DatagramPacketEncoder<Message>(new ProtobufEncoder()));
							//消息处理器
							pipeline.addLast(busyGroup, new ServerHandler());
							//ack处理器
//							 pipeline.addLast("ackHandler", new UdpAckServerHandler());

							pipeline.addLast("timeout", new IdleStateHandler(180, 0, 0,TimeUnit.SECONDS));
							// 心跳处理handler
							// pipeline.addLast(new UdpHeartBeatServerHandler());

						}
					})
					.option(ChannelOption.SO_BROADCAST, true)// 支持广播
					.option(ChannelOption.SO_BACKLOG, 128)
					.option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
					.option(ChannelOption.SO_SNDBUF, 1024 * 1024);// 设置UDP写缓冲区为1M
 
			// 绑定端口，开始接收进来的连接
			channelFuture = b.bind(port).sync();
 
			channel = channelFuture.channel();

		} catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			if (channelFuture != null && channelFuture.isSuccess()) {
				log.info("Netty started on port(s): {} (udp)",port);
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
		log.info("Shutdown Netty UDP Server...");
		if(channel != null) { channel.close();}
		workerGroup.shutdownGracefully();
		log.info("Shutdown Netty UDP Server Success!");
	}
 
}
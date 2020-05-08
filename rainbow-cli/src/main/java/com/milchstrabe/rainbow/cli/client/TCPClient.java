package com.milchstrabe.rainbow.cli.client;

import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ch3ng
 * @Date 2020/4/28 20:44
 * @Version 1.0
 * @Description
 **/
public class TCPClient {

	public static Map<String,Object> SERVER_NODE = null;

	private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	public static ChannelFuture f = null;
	public void start() {
		String remoteHost = SERVER_NODE.get("host").toString();
		int port = ((Double) SERVER_NODE.get("tcpPort")).intValue();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new ProtobufVarint32FrameDecoder());
					pipeline.addLast(new ProtobufDecoder(Data.Response.getDefaultInstance()));
					pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
					pipeline.addLast(new ProtobufEncoder());
					pipeline.addLast(new ProtoTCPClientHandler());
				}
			});
			bootstrap.remoteAddress(remoteHost, port);
			f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
				final EventLoop eventLoop = futureListener.channel().eventLoop();
				if (!futureListener.isSuccess()) {
					eventLoop.schedule(() -> start(), 10, TimeUnit.SECONDS);
				}
			});
			f.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destory(){
		if(f != null){
			f.channel().close();
		}
		eventLoopGroup.shutdownGracefully();

	}
}
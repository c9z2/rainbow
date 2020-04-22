package com.milchstrabe.rainbow.skt.server.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ch3ng
 * @Date 2020/4/22 22:34
 * @Version 1.0
 * @Description
 **/
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

	}
 
	 @Override
	 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
	
	 // 消息处理。。。。。
	 
	 //消息发送。。。。

	 }

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
	}
}

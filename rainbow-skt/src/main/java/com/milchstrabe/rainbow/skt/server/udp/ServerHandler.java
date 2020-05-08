package com.milchstrabe.rainbow.skt.server.udp;

import com.milchstrabe.rainbow.skt.common.constant.StateCode;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.tcp.scanner.Invoker;
import com.milchstrabe.rainbow.skt.server.tcp.scanner.InvokerHolder;
import com.milchstrabe.rainbow.skt.server.session.NettySession;
import com.milchstrabe.rainbow.skt.server.session.Session;
import com.milchstrabe.rainbow.skt.server.udp.codc.UDPRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ch3ng
 * @Date 2020/4/22 22:34
 * @Version 1.0
 * @Description
 **/
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Data.Request> {


	private void handlerMessage(UDPRequest udpRequest) {
		Data.Response response = null;
		int firstOrder = udpRequest.getRequest().getCmd1();
		int secondOrder = udpRequest.getRequest().getCmd2();
		Session session = udpRequest.getSession();
		Invoker invoker = InvokerHolder.getInvoker(firstOrder, secondOrder);
		if (invoker != null) {
			//指令
			Object invoke = invoker.invoke(udpRequest);
			if (invoke == null) {
				return;
			}
			response = (Data.Response) invoke;

			session.write(response);
		} else {
			log.info("没有相关的指令");
			response = Data.Response
					.newBuilder()
					.setCmd1(firstOrder)
					.setCmd2(secondOrder)
					.setCode(StateCode.NOT_FOUND)
					.build();
			session.write(response);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Data.Request request) throws Exception {
		UDPRequest tcpRequest = UDPRequest.builder()
				.request(request)
				.session(new NettySession(channelHandlerContext.channel()))
				.build();
		handlerMessage(tcpRequest);
	}
}

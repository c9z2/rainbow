package com.milchstrabe.rainbow.skt.server.typ3.udp;

import com.milchstrabe.rainbow.skt.common.constant.StateCode;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.scanner.Invoker;
import com.milchstrabe.rainbow.skt.server.scanner.InvokerHolder;
import com.milchstrabe.rainbow.skt.server.session.NettySession;
import com.milchstrabe.rainbow.skt.server.session.Request;
import com.milchstrabe.rainbow.skt.server.session.Session;
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


	private void handlerMessage(Request request) {
		Data.Response response = null;
		int firstOrder = request.getRequest().getCmd1();
		int secondOrder = request.getRequest().getCmd2();
		Session session = request.getSession();
		Invoker invoker = InvokerHolder.getInvoker(firstOrder, secondOrder);
		if (invoker != null) {
			//指令
			Object invoke = invoker.invoke(request);
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
		Request tcpRequest = Request.builder()
				.request(request)
				.session(new NettySession(channelHandlerContext.channel()))
				.build();
		handlerMessage(tcpRequest);
	}
}

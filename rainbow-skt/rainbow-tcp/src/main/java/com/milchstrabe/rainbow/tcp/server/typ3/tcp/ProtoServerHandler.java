package com.milchstrabe.rainbow.tcp.server.typ3.tcp;

import com.milchstrabe.rainbow.base.common.constant.SessionKey;
import com.milchstrabe.rainbow.base.common.constant.StateCode;
import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.base.server.scanner.Invoker;
import com.milchstrabe.rainbow.base.server.scanner.InvokerHolder;
import com.milchstrabe.rainbow.base.server.session.*;
import com.milchstrabe.rainbow.server.domain.UCI;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ch3ng
 * @Date 2020/4/27 20:30
 * @Version 1.0
 * @Description
 **/
@Slf4j
public class ProtoServerHandler extends SimpleChannelInboundHandler<Data.Request> {

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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Data.Request request){
        Request tcpRequest = Request.builder()
                .request(request)
                .session(new NettySession(channelHandlerContext.channel()))
                .build();
        handlerMessage(tcpRequest);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                log.info("20s ,do not read and write");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("connection err!");
        Channel channel = ctx.channel();
        Attribute<SessionAttribute> attachment = channel.attr(AttributeKey.valueOf("ATTACHMENT_KEY"));
        if (attachment != null) {
            SessionAttribute sessionAttribute =  attachment.get();
            UCI uci = (UCI) sessionAttribute.get(SessionKey.CLIENT_IN_SESSION);
            if(uci != null){
                SessionManager.removeSession(uci.getUsername(),uci.getCid());
            }

        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }
}
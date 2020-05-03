package com.milchstrabe.rainbow.skt.server.tcp;

import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.skt.common.constant.SessionKey;
import com.milchstrabe.rainbow.skt.common.constant.StateCode;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPRequest;
import com.milchstrabe.rainbow.skt.server.tcp.scanner.Invoker;
import com.milchstrabe.rainbow.skt.server.tcp.scanner.InvokerHolder;
import com.milchstrabe.rainbow.skt.server.tcp.session.NettySession;
import com.milchstrabe.rainbow.skt.server.tcp.session.Session;
import com.milchstrabe.rainbow.skt.server.tcp.session.SessionAttribute;
import com.milchstrabe.rainbow.skt.server.tcp.session.SessionManager;
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

    private void handlerMessage(TCPRequest TCPRequest) {
        Data.Response response = null;
        int firstOrder = TCPRequest.getRequest().getCmd1();
        int secondOrder = TCPRequest.getRequest().getCmd2();
        Session session = TCPRequest.getSession();
        //此处应该加入拦截器
        //拦截器的构思还没想好
        Invoker invoker = InvokerHolder.getInvoker(firstOrder, secondOrder);

        if (invoker != null) {
            //指令
            Object invoke = invoker.invoke(TCPRequest);
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
        TCPRequest tcpRequest = TCPRequest.builder()
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
                //close connection
                //open udp datagram Channel
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
            User user = (User) sessionAttribute.get(SessionKey.CLIENT_IN_SESSION);
            //remove session object
            if(user != null){
                SessionManager.removeSession(user.getId());
            }

        }

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }
}
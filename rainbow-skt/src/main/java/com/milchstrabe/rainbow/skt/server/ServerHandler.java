package com.milchstrabe.rainbow.skt.server;

import com.milchstrabe.rainbow.skt.server.codc.Request;
import com.milchstrabe.rainbow.skt.server.codc.Response;
import com.milchstrabe.rainbow.skt.common.constant.SessionKey;
import com.milchstrabe.rainbow.skt.server.scanner.Invoker;
import com.milchstrabe.rainbow.skt.server.scanner.InvokerHolder;
import com.milchstrabe.rainbow.skt.server.session.NettySession;
import com.milchstrabe.rainbow.skt.server.session.Session;
import com.milchstrabe.rainbow.skt.server.session.SessionAttribute;
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
 * @Date 2020/4/20 21:21
 * @Version 1.0
 * @Description
 **/
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    private void handlerMessage(Request request) {
        Response response = null;
        byte firstOrder = request.getFirstOrder();
        byte secondOrder = request.getSecondOrder();
        Session session = request.getSession();
        //此处应该加入拦截器
        //拦截器的构思还没想好
        Invoker invoker = InvokerHolder.getInvoker(firstOrder, secondOrder);

        if (invoker != null) {
            //指令
            Object invoke = invoker.invoke(request);
            if (invoke == null) {
                return;
            }
            response = (Response) invoke;

            session.write(response);
        } else {
            log.info("没有相关的指令");
            response = new Response(request, StateCode.NOT_FOUND);
            session.write(response);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request){
        request.setSession(new NettySession(channelHandlerContext.channel()));
        handlerMessage(request);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                // 清除连接会话
                log.info("20s,未发生读写操作，心跳超时清除会话连接");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        Attribute<SessionAttribute> attachment = channel.attr(AttributeKey.valueOf("ATTACHMENT_KEY"));
        if (attachment != null) {
            SessionAttribute sessionAttribute =  attachment.get();
            Object o = sessionAttribute.get(SessionKey.CLIENT_IN_SESSION);
            //透传掉线信息
//            SessionManager.removeSession(cid);
        }

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // logger.info("========"+cause.getMessage());
//        super.exceptionCaught(ctx, cause);
    }
}

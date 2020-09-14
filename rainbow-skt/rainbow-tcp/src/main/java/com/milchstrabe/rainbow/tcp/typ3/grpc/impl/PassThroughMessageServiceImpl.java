package com.milchstrabe.rainbow.tcp.typ3.grpc.impl;

import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.base.server.typ3.grpc.Msg;
import com.milchstrabe.rainbow.base.server.typ3.grpc.PassThroughMessageServiceGrpc;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.tcp.common.constant.StateCode;
import com.milchstrabe.rainbow.tcp.typ3.netty.session.Session;
import com.milchstrabe.rainbow.tcp.typ3.netty.session.SessionManager;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author ch3ng
 * @Date 2020/4/25 16:13
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class PassThroughMessageServiceImpl extends PassThroughMessageServiceGrpc.PassThroughMessageServiceImplBase {

    private final static int CMD1 = 1;
    private final static int CMD2 = 1;
    /**
     * default cmd1:1
     * default cmd2:1
     * @param request
     * @param responseObserver
     */
    @Override
    public void passThroughMessage(Msg.MsgRequest request, StreamObserver<Msg.MsgResponse> responseObserver) {
        String receiver = request.getReceiver();
        Msg.MsgResponse msgResponse = null;
        try {
            Map<String, Session> sessions = SessionManager.getSession(receiver);
            Data.Response resp = Data.Response.newBuilder()
                    .setCmd1(CMD1)
                    .setCmd2(CMD2)
                    .setCode(StateCode.SUCCESS)
                    .setData(request.toByteString())
                    .build();
            Iterator<Map.Entry<String, Session>> iterator = sessions.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Session> sessionEntry = iterator.next();
                Session session = sessionEntry.getValue();
                session.write(resp);
            }
            msgResponse = Msg.MsgResponse.newBuilder()
                    .setCode(200)
                    .setMsg("success")
                    .build();
        } catch (LogicException e) {
            log.error(e.getMessage());
            log.error("current server node can not find target");
            msgResponse = Msg.MsgResponse.newBuilder()
                    .setCode(300)
                    .setMsg("fail")
                    .build();
        }
        responseObserver.onNext(msgResponse);
        responseObserver.onCompleted();
    }
}

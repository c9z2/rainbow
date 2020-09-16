package com.milchstrabe.rainbow.ws.typ3.grpc.impl;

import com.alibaba.fastjson.JSON;
import com.milchstrabe.rainbow.api.typ3.grpc.Msg;
import com.milchstrabe.rainbow.api.typ3.grpc.PassThroughMessageServiceGrpc;
import com.milchstrabe.rainbow.server.domain.po.Message;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * @Author ch3ng
 * @Date 2020/4/25 16:13
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class PassThroughMessageServiceImpl extends PassThroughMessageServiceGrpc.PassThroughMessageServiceImplBase {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * default cmd1:1
     * default cmd2:1
     * @param request
     * @param responseObserver
     */
    @Override
    public void passThroughMessage(Msg.MsgRequest request, StreamObserver<Msg.MsgResponse> responseObserver) {

        Message<Object> message = new Message<>();
        message.setId(request.getMsgId());
        message.setStatus((short)1);
        message.setSender(request.getSender());
        message.setReceiver(request.getReceiver());
        message.setMsgType(request.getMsgType());
        message.setDate(request.getDate());
        message.setContent(JSON.parseObject(request.getContent()));

        String json = JSON.toJSONString(message);
        log.info(json);

        simpMessageSendingOperations.convertAndSendToUser(request.getReceiver(), "/message", json);
        Msg.MsgResponse msgResponse = Msg.MsgResponse.newBuilder()
                .setCode(200)
                .setMsg("success")
                .build();

        responseObserver.onNext(msgResponse);
        responseObserver.onCompleted();
    }
}

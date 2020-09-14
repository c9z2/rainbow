package com.milchstrabe.rainbow.ws.typ3.grpc.impl;

import com.milchstrabe.rainbow.api.typ3.grpc.Msg;
import com.milchstrabe.rainbow.api.typ3.grpc.PassThroughMessageServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * default cmd1:1
     * default cmd2:1
     * @param request
     * @param responseObserver
     */
    @Override
    public void passThroughMessage(Msg.MsgRequest request, StreamObserver<Msg.MsgResponse> responseObserver) {
        String receiver = request.getReceiver();
        Msg.MsgResponse msgResponse = Msg.MsgResponse.newBuilder()
                .setCode(200)
                .setMsg("success")
                .build();;

        responseObserver.onNext(msgResponse);
        responseObserver.onCompleted();
    }
}

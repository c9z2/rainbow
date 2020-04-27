package com.milchstrabe.rainbow.skt.server.grpc.impl;

import com.milchstrabe.rainbow.skt.server.grpc.Msg;
import com.milchstrabe.rainbow.skt.server.grpc.PassThroughMessageServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

/**
 * @Author ch3ng
 * @Date 2020/4/25 16:13
 * @Version 1.0
 * @Description
 **/
@Component
public class PassThroughMessageServiceImpl extends PassThroughMessageServiceGrpc.PassThroughMessageServiceImplBase {

    @Override
    public void passThroughMessage(Msg.MsgRequest request, StreamObserver<Msg.MsgResponse> responseObserver) {
        super.passThroughMessage(request, responseObserver);
    }
}

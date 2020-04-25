package com.milchstrabe.rainbow.skt.server.grpc.impl;

import com.milchstrabe.rainbow.skt.server.grpc.PassThroughMessageServiceGrpc;
import com.milchstrabe.rainbow.skt.server.grpc.TxtMsg;
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
    public void passThroughMessage(TxtMsg.TxtMsgRequest request, StreamObserver<TxtMsg.Response> responseObserver) {
        super.passThroughMessage(request, responseObserver);
    }
}

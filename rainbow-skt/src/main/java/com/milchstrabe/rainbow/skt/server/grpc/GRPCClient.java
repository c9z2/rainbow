package com.milchstrabe.rainbow.skt.server.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GRPCClient {


  public TxtMsg.Response sendMsg(TxtMsg.TxtMsgRequest txtMsgRequest,ManagedChannel channel) {

//    TxtMsg.TxtMsgRequest build = TxtMsg.TxtMsgRequest.newBuilder(txtMsgRequest).build();
      PassThroughMessageServiceGrpc.PassThroughMessageServiceBlockingStub passThroughMessageServiceBlockingStub = PassThroughMessageServiceGrpc.newBlockingStub(channel);
      TxtMsg.Response response = passThroughMessageServiceBlockingStub.passThroughMessage(txtMsgRequest);
      log.info("msg: " + response.getMsg());
      return response;
  }


  public void sender(String host, int port, TxtMsg.TxtMsgRequest request) throws Exception {
    String target = host+ ":"+ port;
    // Allow passing in the user and target strings as command line arguments

    // Create a communication channel to the server, known as a Channel. Channels are thread-safe
    // and reusable. It is common to create channels at the beginning of your application and reuse
    // them until the application shuts down.
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
        .usePlaintext()
        .build();
    try {
      TxtMsg.Response response = sendMsg(request, channel);
    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
      // resources the channel should be shut down when it will no longer be used. If it may be used
      // again leave it running.
      channel.shutdownNow();//.awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
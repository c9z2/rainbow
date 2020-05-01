package com.milchstrabe.rainbow.cli.interpret;

import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.cli.client.TCPClient;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.grpc.Msg;

import java.util.UUID;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:42
 * @Version 1.0
 * @Description
 **/
public class SendExpression implements CMDExpression {

    @Override
    public boolean interpret(String[] cmds) {
        String msg = cmds[1];

        /**
         *  string msgId = 1;
         *     int32 msgType = 2;
         *     string content = 3;
         *     string sender = 4;
         *     string receiver = 5;
         *     string date = 6;
         */
        Msg.MsgRequest msgRequest = Msg.MsgRequest.newBuilder()
                .setMsgId(UUID.randomUUID().toString())
                .setMsgType(1)
                .setContent(msg)
                .setSender("admin")
                .setReceiver("root")
                .setDate("2020-05-02 19:00:00")
                .build();

        ByteString bytes = msgRequest.toByteString();
        Data.Request request = Data.Request.newBuilder()
                .setCmd1(1)
                .setCmd2(1)
                .setData(bytes)
                .build();
        TCPClient.f.channel().writeAndFlush(request);
        return true;
    }
}

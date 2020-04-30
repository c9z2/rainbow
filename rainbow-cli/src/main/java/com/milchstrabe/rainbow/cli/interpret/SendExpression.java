package com.milchstrabe.rainbow.cli.interpret;

import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.cli.client.TCPClient;
import com.milchstrabe.rainbow.skt.server.codc.Data;

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
        Data.Request request = Data.Request.newBuilder()
                .setCmd1(1)
                .setCmd2(1)
                .setData(ByteString.copyFromUtf8(msg))
                .build();
        TCPClient.f.channel().writeAndFlush(request);
        return true;
    }
}

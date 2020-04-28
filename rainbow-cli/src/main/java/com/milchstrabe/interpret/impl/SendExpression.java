package com.milchstrabe.interpret.impl;

import com.google.protobuf.ByteString;
import com.milchstrabe.client.TCPClient;
import com.milchstrabe.interpret.Expression;
import com.milchstrabe.rainbow.client.codc.Data;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:42
 * @Version 1.0
 * @Description
 **/
public class SendExpression implements Expression {

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

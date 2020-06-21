package com.milchstrabe.rainbow.cli.controller;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.base.server.typ3.grpc.Msg;
import com.milchstrabe.rainbow.cli.annotion.NettyController;
import com.milchstrabe.rainbow.cli.annotion.NettyMapping;
import io.netty.channel.Channel;


/**
 * @Author ch3ng
 * @Date 2020/5/8 21:11
 * @Version 1.0
 * @Description
 **/
@NettyController(cmd = 1)
public class MessageController extends AbstractController{


    @NettyMapping(cmd = 1)
    @Override
    public void resp(Data.Response response, Channel channel) {
        ByteString data = response.getData();
        try {
            Msg.MsgResponse msgResponse = Msg.MsgResponse.parseFrom(data);
            System.out.println(msgResponse);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }
}

package com.milchstrabe.rainbow.cli.controller;

import com.milchstrabe.rainbow.cli.annotion.NettyController;
import com.milchstrabe.rainbow.cli.annotion.NettyMapping;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.channel.Channel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author ch3ng
 * @Date 2020/5/8 21:57
 * @Version 1.0
 * @Description
 **/
@NettyController(cmd = 0)
public class SignInController extends AbstractController{

    Timer timer = new Timer();

    @NettyMapping(cmd = 0)
    @Override
    public void resp(Data.Response response, Channel channel) {
        int code = response.getCode();
        if(code == 2){
            //sign in success
            new Thread(()->{
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Data.Request request = Data.Request.newBuilder()
                                .setCmd1(0)
                                .setCmd2(1)
                                .build();
                        channel.writeAndFlush(request);
                    }
                },0,30000);
            }).start();
        }else{
            //sign in fail
            System.out.println("sign in fail");
        }
    }
}

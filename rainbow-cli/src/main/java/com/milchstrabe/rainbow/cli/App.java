package com.milchstrabe.rainbow.cli;

import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.cli.client.TCPClient;
import com.milchstrabe.rainbow.cli.common.Constant;
import com.milchstrabe.rainbow.cli.interpret.CMDExpression;
import com.milchstrabe.rainbow.cli.interpret.CMDS;
import com.milchstrabe.rainbow.skt.server.codc.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * rainbow client test program
 *
 */
public class App{

    public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        TCPClient client = new TCPClient();
        new Thread(()->{
            client.start();
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutdown tcp connection");
            client.destory();
        }));

        Scanner inp = new Scanner(System.in);

        login(inp);

        while (true){
            System.out.print("$ ");
            String str = inp.nextLine();

            if(str == null || "".equals(str = str.trim())){
                str = "command is error";
            }

            String regex = "\\s+";
            String[] split = str.split(regex);
            if(split.length>=1){
                CMDExpression expression = CMDS.C_M_D_S.get(split[0]);
                if(expression != null){
                    expression.interpret(split);
                }
            }
            System.out.println("command not found: "+ split[0]);
        }

    }

    private static void login(Scanner inp){
        System.out.print("username: ");
        String username = inp.nextLine();
        System.out.print("password: ");
        String password = inp.nextLine();

        Map<String,Object> param = new HashMap<>();
        param.put("username",username);
        param.put("password",password);
        String post = HttpUtil.post(Constant.sign_In, param);
        /**
         *     private Integer code;
         *     private String msg;
         *     private T data;
         */
        Gson gson = new Gson();
        Result result = gson.fromJson(post, Result.class);
        if(200 == result.getCode()){
            String token = result.getData().toString();
            Data.Request request = Data.Request.newBuilder()
                    .setCmd1(0)
                    .setCmd2(0)
                    .setData(ByteString.copyFromUtf8(token))
                    .build();
            TCPClient.f.channel().writeAndFlush(request);
            return;
        }
        login(inp);
    }


}

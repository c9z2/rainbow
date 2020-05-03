package com.milchstrabe.rainbow.cli;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.cli.client.TCPClient;
import com.milchstrabe.rainbow.cli.client.UDPClient;
import com.milchstrabe.rainbow.cli.common.Constant;
import com.milchstrabe.rainbow.cli.interpret.CMDExpression;
import com.milchstrabe.rainbow.cli.interpret.CMDS;
import com.milchstrabe.rainbow.skt.server.codc.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * rainbow client test program
 *
 */
public class App{

    static TCPClient tcpClient = new TCPClient();
    static UDPClient udpClient = new UDPClient();

    public static void main( String[] args ) {


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
                    continue;
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
        String post = HttpUtil.post(Constant.SIGN_IN, param);
        /**
         *     private Integer code;
         *     private String msg;
         *     private T data;
         */
        Gson gson = new Gson();
        Result result = gson.fromJson(post, Result.class);
        if(200 == result.getCode()){
            String token = result.getData().toString();
            HttpRequest get = HttpUtil.createGet(Constant.SERVER_NODE);
            get.header("Authorization","Berarer " + token);
            HttpResponse execute = get.execute();
            if(!execute.isOk()){
                login(inp);
            }
            String body = execute.body();
            Result serverNodeResult = gson.fromJson(body,Result.class);
            Map<String,Object> map =  (Map) serverNodeResult.getData();
            TCPClient.SERVER_NODE = map;


            new Thread(()->{
                tcpClient.start();
            }).start();

            new Thread(()->{
                udpClient.start();
            }).start();


            new Thread(()->{
                try {
                    while (udpClient.channel == null){
                        Thread.sleep(500);
                    }
                    udpClient.channel.closeFuture().await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(()->{
                //tcp server
                try {
                    while(tcpClient.f == null ){
                        Thread.sleep(500);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                tcpClient.f.channel().closeFuture().syncUninterruptibly();
            }).start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("shutdown tcp connection");
                tcpClient.destory();
                udpClient.destory();
            }));

            Data.Request request = Data.Request.newBuilder()
                    .setCmd1(0)
                    .setCmd2(0)
                    .setData(ByteString.copyFromUtf8(token))
                    .build();
            try {
                while (tcpClient.f == null){
                    Thread.sleep(500);
                }
            }catch (Exception e){

            }

            tcpClient.f.channel().writeAndFlush(request);
            return;
        }
        login(inp);
    }


}

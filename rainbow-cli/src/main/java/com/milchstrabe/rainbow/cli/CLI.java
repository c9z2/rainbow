package com.milchstrabe.rainbow.cli;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.cli.client.TCPClient;
import com.milchstrabe.rainbow.cli.client.UDPClient;
import com.milchstrabe.rainbow.cli.common.Constant;
import com.milchstrabe.rainbow.cli.controller.ControllerContext;
import com.milchstrabe.rainbow.cli.interpret.CMDExpression;
import com.milchstrabe.rainbow.cli.interpret.CMDS;
import com.milchstrabe.rainbow.skt.server.codc.Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * rainbow client test program
 *
 */
public class CLI{

    static TCPClient tcpClient = new TCPClient();
    static UDPClient udpClient = new UDPClient();
    public static ControllerContext controllerContext = new ControllerContext();

    public static User user = null;
    static String props = null;

    static {
        props = System.getProperties().getProperty("os.name");;
    }

    /**
     * opCommad root -c
     *
     * @param args
     */
    public static void main( String[] args ) {
        Scanner inp = new Scanner(System.in);
        login(inp);

        while (true){
            String str = inp.nextLine();
            if(str == null || "".equals(str = str.trim())){
                str = "command is error";
            }

            String regex = "\\s+";
            String[] split = str.split(regex);
            if(split.length>=1){
                CMDExpression expression = CMDS.C_M_D_S.get(split[0]);
                if(expression != null){
                    expression.interpret(str);
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
        Result result = null;
        try {
            result = gson.fromJson(post, Result.class);
        }catch (Exception e){
            System.out.println("sign in fail: " + e.getMessage());
        }

        if(200 == result.getCode()){
            String token = result.getData().toString();
            HttpRequest get = HttpUtil.createGet(Constant.SERVER_NODE);

            get.header("Authorization","Berarer " + token);
            HttpResponse execute = get.execute();
            if(!execute.isOk()){
                login(inp);
            }

            DecodedJWT decode = JWT.decode(token);
            String userId = decode.getClaim("userId").asString();
            String username_ = decode.getClaim("username").asString();
            user = User.builder()
                    .username(username_)
                    .id(userId)
                    .build();

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
            String cliStr = null;
            if(props.startsWith("Linux") || props.startsWith("MAC")){
                File file = new File("~/.rainbow");
                if(file.exists()){
                    FileReader fileReader = null;
                    BufferedReader bufferedReader = null;
                    try {
                        fileReader = new FileReader(file);
                        bufferedReader = new BufferedReader(fileReader);
                        cliStr = bufferedReader.readLine();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(fileReader != null){
                                fileReader.close();
                            }
                            if(bufferedReader !=  null){
                                bufferedReader.close();
                            }
                        }catch (IOException e){
                            System.out.println(e.getMessage());
                        }
                    }

                }
            }else{

            }
            if(cliStr == null || "".equals(cliStr)){
                HttpRequest postCLI = HttpUtil.createPost(Constant.CID);
                get.header("Authorization","Berarer " + token);
                HttpResponse executeCLI = postCLI.execute();
                if(!executeCLI.isOk()){
                    login(inp);
                }
                cliStr = executeCLI.body();
                //write cli info to file
                if(props.startsWith("Linux") || props.startsWith("MAC")){
                    File file = new File("~/.rainbow");
                    if(file.exists()){
                        FileWriter writer = null;
                        BufferedWriter bufferedWriter = null;
                        try {
                            writer = new FileWriter(file);
                            bufferedWriter = new BufferedWriter(writer);
                            bufferedWriter.write(cliStr);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if(writer != null){
                                    writer.close();
                                }
                                if(bufferedWriter !=  null){
                                    bufferedWriter.close();
                                }
                            }catch (IOException e){
                                System.out.println(e.getMessage());
                            }
                        }

                    }
                }else{

                }
            }
            /**
             *  request->{
             *      "token":"jwt",
             *      "cid":"cid",
             *      "clientType":"MACOS"
             *  }
             */
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("token",token);
            jsonObject.addProperty("cid", cliStr);
            jsonObject.addProperty("clientType",props);
            String json = gson.toJson(jsonObject);
            Data.Request request = Data.Request.newBuilder()
                    .setCmd1(0)
                    .setCmd2(0)
                    .setData(ByteString.copyFromUtf8(json))
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

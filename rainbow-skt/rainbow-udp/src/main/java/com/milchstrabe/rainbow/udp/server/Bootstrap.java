package com.milchstrabe.rainbow.udp.server;

import com.milchstrabe.rainbow.base.server.typ3.grpc.GRPCServer;
import com.milchstrabe.rainbow.udp.server.typ3.udp.NettyUDPServer;
import io.grpc.Server;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author ch3ng
 * @Date 2020/6/5 10:32
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Order(2)
@Component
public class Bootstrap implements CommandLineRunner {

    @Value("${netty.udp.port:8082}")
    private int udpPort;
    @Value("${netty.gRPC.port:8083}")
    private int gRPCPort;

    @Autowired
    private NettyUDPServer nettyUDPServer;

    @Autowired
    private GRPCServer grpcServer;

    @Override
    public void run(String... args){
        //udp
        ChannelFuture udpFuture = nettyUDPServer.start(udpPort);
        //gRPC
        Server server = grpcServer.start(gRPCPort);


        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                //udp
                nettyUDPServer.destroy();

                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                // shutting down gRPC server since JVM is shutting down
                grpcServer.destroy();
            }
        });


        new Thread(()->{
            //udp server
            try {
                udpFuture.channel().closeFuture().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();


        new Thread(()->{
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }).start();

    }
}

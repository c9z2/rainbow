package com.milchstrabe.rainbow.tcp.typ3.grpc;

import com.milchstrabe.rainbow.base.server.typ3.grpc.PassThroughMessageServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ch3ng
 * @Date 2020/4/23 00:57
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class GRPCServer {

    @Autowired
    private PassThroughMessageServiceGrpc.PassThroughMessageServiceImplBase passThroughMessageServiceImpl;

    private Server server;

    public Server start(int port) {

        try {
            this.server = ServerBuilder.forPort(port)
                    .addService(passThroughMessageServiceImpl)
                    .build()
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            destroy();
        }finally {
            if(this.server != null){
                log.info("gRPC started on port(s): {} (gRPC)",port);
            }else{
                log.error("gRPC started Error!");
            }
        }
        return server;
    }

    public void destroy() {
        log.info("Shutdown gRPC Server...");
        if (server != null) {
            server.shutdown();
            log.info("Shutdown gRPC Server Success!");
        }
    }

}

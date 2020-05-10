package com.milchstrabe.rainbow.skt;

import com.milchstrabe.rainbow.skt.server.typ3.grpc.GRPCServer;
import com.milchstrabe.rainbow.skt.server.typ3.tcp.NettyTCPServer;
import com.milchstrabe.rainbow.skt.server.typ3.udp.NettyUDPServer;
import io.grpc.Server;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RainbowSktApplication implements CommandLineRunner {

	@Value("${netty.tcp.port:8081}")
	private int tcpPort;
	@Value("${netty.udp.port:8082}")
	private int udpPort;
	@Value("${netty.gRPC.port:8083}")
	private int gRPCPort;

	@Autowired
	private NettyTCPServer nettyTCPServer;

	@Autowired
	private NettyUDPServer nettyUDPServer;

	@Autowired
	private GRPCServer grpcServer;


	public static void main(String[] args) {
		SpringApplication.run(RainbowSktApplication.class, args);
	}

	@Override
	public void run(String... args){
		//tcp
		ChannelFuture tcpFuture = nettyTCPServer.start(tcpPort);

		//udp
		ChannelFuture udpFuture = nettyUDPServer.start(udpPort);
		//gRPC
		Server server = grpcServer.start(gRPCPort);


		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				//tcp
				nettyTCPServer.destroy();
				//udp
				nettyUDPServer.destroy();

				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				// shutting down gRPC server since JVM is shutting down
				grpcServer.destroy();
			}
		});


		new Thread(()->{
			//tcp server
			tcpFuture.channel().closeFuture().syncUninterruptibly();
		}).start();

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

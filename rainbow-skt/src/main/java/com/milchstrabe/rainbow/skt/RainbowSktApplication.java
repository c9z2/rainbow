package com.milchstrabe.rainbow.skt;

import com.milchstrabe.rainbow.skt.server.tcp.NettyTCPServer;
import com.milchstrabe.rainbow.skt.server.udp.NettyUDPServer;
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

	@Autowired
	private NettyTCPServer nettyTCPServer;

	@Autowired
	private NettyUDPServer nettyUDPServer;

	public static void main(String[] args) {
		SpringApplication.run(RainbowSktApplication.class, args);
	}

	@Override
	public void run(String... args){
		//tcp
		ChannelFuture tcpFuture = nettyTCPServer.start(tcpPort);

		//udp
		ChannelFuture udpFuture = nettyUDPServer.start(udpPort);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				//tcp
				nettyTCPServer.destroy();
				//udp
				nettyUDPServer.destroy();
			}
		});
		//tcp server
		tcpFuture.channel().closeFuture().syncUninterruptibly();

		//udp server
		try {
			udpFuture.channel().closeFuture().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}

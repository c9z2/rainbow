package com.milchstrabe.rainbow.skt;

import com.milchstrabe.rainbow.skt.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RainbowSktApplication implements CommandLineRunner {

	@Value("${netty.port:8081}")
	private int port;

	@Autowired
	private NettyServer nettyServer;

	public static void main(String[] args) {
		SpringApplication.run(RainbowSktApplication.class, args);
	}

	@Override
	public void run(String... args){
		ChannelFuture future = nettyServer.start(port);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				nettyServer.destroy();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}

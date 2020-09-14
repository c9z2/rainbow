package com.milchstrabe.rainbow.ws.common.config;

import com.milchstrabe.rainbow.ws.typ3.zk.ServerByCurator;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ch3ng
 * @Date 2020/4/26 17:51
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Configuration
public class CuratorConfig {

    @Value("${curator.retryCount}")
    private int retryCount;

    @Value("${curator.elapsedTimeMs}")
    private int elapsedTimeMs;

    @Value("${curator.connectString}")
    private String connectString;

    @Value("${curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;

    @Value("${curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Autowired
    private ServerByCurator serverByCurator;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                new RetryNTimes(retryCount, elapsedTimeMs));
         curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
             @Override
             public void stateChanged(CuratorFramework client, ConnectionState newState) {
                 System.out.println(newState);
                 System.out.println(client);
                 switch (newState){
                     case CONNECTED:
                     case RECONNECTED:

                         //register current server to zookeeper
                         try {
                             serverByCurator.createNode(null,0);
                             serverByCurator.setData2Node(null);
                         } catch (Exception e) {
                             log.error(e.getMessage());
                         }

                 }

             }
         });

        return curatorFramework;
    }
}
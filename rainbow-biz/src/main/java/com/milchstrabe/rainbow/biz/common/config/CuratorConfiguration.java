package com.milchstrabe.rainbow.biz.common.config;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milchstrabe.rainbow.biz.common.ServerNodesCache;
import com.milchstrabe.rainbow.server.domain.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @Author ch3ng
 * @Date 2020/4/26 17:51
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Configuration
public class CuratorConfiguration {

    private static final String ROOT_PATH = "/rainbow";

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
                switch (newState){
                    case SUSPENDED:
                        break;
                    case CONNECTED:
                    case RECONNECTED:
                        try {
                            List<String> subPaths = client.getChildren().forPath(ROOT_PATH);
                            ObjectMapper objectMapper = new ObjectMapper();
                            for(String subPath : subPaths){
                                byte[] bytes = client.getData().forPath(ROOT_PATH + "/" + subPath);
                                Node znode = objectMapper.readValue(new String(bytes, Charset.forName("utf-8")), Node.class);
                                ServerNodesCache.existUpdateOrAdd(znode);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    case LOST:
                        break;
                    case READ_ONLY:
                        break;
                }
            }
        });

        Consumer<CuratorFramework> consumer = client ->{
            ExecutorService pool = Executors.newCachedThreadPool();
            //设置节点的cache
            TreeCache treeCache = new TreeCache(client, ROOT_PATH);
            //设置监听器和处理过程
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

                    ChildData data = event.getData();
                    if(data !=null && data.getData() != null && data.getData().length>0){
                        Node znode = null;
                        ObjectMapper objectMapper = new ObjectMapper();
                        switch (event.getType()) {
                            case NODE_ADDED:
                                log.info("NODE_ADDED : [{}],data : [{}]", data.getPath(), new String(data.getData()));
                                break;
                            case NODE_REMOVED:
                                log.info("NODE_REMOVED : [{}],data : [{}]", data.getPath(), new String(data.getData()));
                                znode =  objectMapper.readValue(new String(data.getData()), Node.class);
                                ServerNodesCache.removeNode(znode);
                                break;
                            case NODE_UPDATED:
                                log.info("NODE_UPDATED : [{}],data : [{}]", data.getPath(), new String(data.getData()));
                                 znode = objectMapper.readValue(new String(data.getData()), Node.class);
                                ServerNodesCache.existUpdateOrAdd(znode);
                                break;
                            default:
                                break;
                        }
                    }else{
                        log.info( "data is null : "+ event.getType());
                    }
                }
            },pool);
            try {
                treeCache.start();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        };

        consumer.accept(curatorFramework);

        return curatorFramework;
    }
}
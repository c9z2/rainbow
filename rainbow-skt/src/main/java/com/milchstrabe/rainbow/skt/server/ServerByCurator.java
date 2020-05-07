package com.milchstrabe.rainbow.skt.server;

import com.google.gson.Gson;
import com.milchstrabe.rainbow.server.domain.Node;
import com.milchstrabe.rainbow.skt.common.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @Author ch3ng
 * @Date 2020/4/26 17:32:21
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class ServerByCurator{

    private final static String ROOT_PATH = "/rainbow";

    @Autowired
    private CuratorFramework curatorFramework;

    @Value("${node.host}")
    private String host;

    @Value("${netty.tcp.port}")
    private int tcpPort;

    @Value("${netty.udp.port}")
    private int udpPort;
    /**
     * create node
     */
    public void createNode() throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + tcpPort;
            if(curatorFramework.checkExists().forPath(keyPath)!=null){
                throw new Exception("the znode exists at:" + keyPath);
            }
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(keyPath);
    }


    /**
     * 移除节点
     * @param path
     * @return
     * @throws Exception
     */
    public boolean removeNode(String path) throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + tcpPort;
        if (curatorFramework.checkExists().forPath(keyPath) != null) {
            curatorFramework.delete().forPath(keyPath);
            return true;
        }
        return false;
    }

    /**
     * set data for znode
     * @return
     * @throws Exception
     */
    public boolean setData2Node() throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + tcpPort;
        Node node = Node.builder()
                .host(host)
                .tcpPort(tcpPort)
                .udpPort(udpPort)
                .playload(0L)
                .build();
        Gson gson = new Gson();
        String znodeJson = gson.toJson(node);
        byte[] bytes = znodeJson.getBytes(Charset.forName("utf-8"));
        Stat stat = curatorFramework.setData().forPath(keyPath, bytes);
        if(stat != null){
            return true;
        }
        return false;
    }

    /**
     * get data from znode
     * @return
     * @throws Exception
     */
    public long getDataFromNode() throws Exception {
        String keyPath = ROOT_PATH + "/" + host;
        byte[] bytes = curatorFramework.getData().forPath(keyPath);
        if(bytes != null && bytes.length>0){
            long l = ByteUtil.bytesToLong(bytes);
            return l;
        }
        return 0L;
    }
}
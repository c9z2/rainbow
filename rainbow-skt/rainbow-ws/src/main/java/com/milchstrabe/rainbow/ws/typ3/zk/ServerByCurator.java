package com.milchstrabe.rainbow.ws.typ3.zk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milchstrabe.rainbow.server.domain.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ServerByCurator {

    private final static String ROOT_PATH = "/rainbow";

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * create node
     */
    public void createNode(String host,int port) throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + port;
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
     * @return
     * @throws Exception
     */
    public boolean removeNode(String host,int port) throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + port;
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
    public boolean setData2Node(String host,int port) throws Exception {
        Node node = Node.builder()
                .host(host)
                .port(port)
                .playload(0L)
                .build();
        return setData2Node(node);
    }

    public boolean setData2Node(Node node) throws Exception {
        String keyPath = ROOT_PATH + "/" + node.getHost() + ":" + node.getPort();
        ObjectMapper objectMapper = new ObjectMapper();
        String znodeJson = objectMapper.writeValueAsString(node);
        byte[] bytes = znodeJson.getBytes(Charset.forName("utf-8"));
        Stat stat = curatorFramework.setData().forPath(keyPath, bytes);
        if(stat != null){
            return true;
        }
        return false;
    }

    public boolean updateData2Node(String host,int port) throws Exception{
        Node dataFromNode = getDataFromNode(host,port);
        long playload = dataFromNode.getPlayload();
        dataFromNode.setPlayload(++playload);
        return setData2Node(dataFromNode);
    }

    /**
     * get data from znode
     * @return
     * @throws Exception
     */
    public Node getDataFromNode(String host,int port) throws Exception {
        String keyPath = ROOT_PATH + "/" + host + ":" + port;
        byte[] bytes = curatorFramework.getData().forPath(keyPath);
        if(bytes == null || bytes.length==0){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Node node = objectMapper.readValue(new String(bytes), Node.class);
        return node;
    }
}
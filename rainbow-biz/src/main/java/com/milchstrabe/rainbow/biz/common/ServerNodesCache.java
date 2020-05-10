package com.milchstrabe.rainbow.biz.common;

import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author ch3ng
 * @Date 2020/5/6 17:02
 * @Version 1.0
 * @Description
 **/
public class ServerNodesCache {

    private static final Set<Node> serverNodes = new TreeSet<>((node1,node2)->{
        if(node1.getPlayload() > node2.getPlayload()){
            return 1;
        }else if(node1.getPlayload() < node2.getPlayload()){
            return -1;
        }else{
            return 0;
        }
    });

    public static Node getNode() throws LogicException {
        if(serverNodes.isEmpty()){
            throw new LogicException(30000,"currently no server nodes are available");
        }
        Node next = serverNodes.iterator().next();
        return next;
    }

    public static void removeNode(Node node){
        Iterator<Node> iterator = serverNodes.iterator();
        while (iterator.hasNext()){
            Node temp = iterator.next();
            String host = node.getHost();
            int tcpPort = node.getTcpPort();
            String path = host + ":" + tcpPort;

            String tempHost = temp.getHost();
            int tempTcpPort = temp.getTcpPort();
            String tempPath = tempHost + ":" + tempTcpPort;
            if(path.equals(tempPath)){
                iterator.remove();
                break;
            }
        }
    }


    public static void existUpdateOrAdd(Node node){
        for(Node temp : serverNodes){
            String host = node.getHost();
            int tcpPort = node.getTcpPort();
            String path = host + ":" + tcpPort;

            String tempHost = temp.getHost();
            int tempTcpPort = temp.getTcpPort();
            String tempPath = tempHost + ":" + tempTcpPort;
            if(path.equals(tempPath)){
                temp.setPlayload(node.getPlayload());
                return;
            }
        }
        serverNodes.add(node);
    }
}

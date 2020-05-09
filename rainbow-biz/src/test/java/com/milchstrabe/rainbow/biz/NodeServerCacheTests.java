package com.milchstrabe.rainbow.biz;

import com.milchstrabe.rainbow.biz.common.ServerNodesCache;
import com.milchstrabe.rainbow.biz.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;
import org.junit.jupiter.api.Test;

/**
 * @Author ch3ng
 * @Date 2020/5/9 14:57
 * @Version 1.0
 * @Description
 **/
public class NodeServerCacheTests {


    @Test
    public void testCompare() throws LogicException {
        Node abc1 = Node.builder().playload(1).udpPort(111).tcpPort(221).host("abc1").build();
        Node abc2 = Node.builder().playload(2).udpPort(112).tcpPort(222).host("abc2").build();
        Node abc3 = Node.builder().playload(3).udpPort(113).tcpPort(223).host("abc3").build();
        ServerNodesCache.existUpdateOrAdd(abc1);
        ServerNodesCache.existUpdateOrAdd(abc2);
        ServerNodesCache.existUpdateOrAdd(abc3);
        Node node = ServerNodesCache.getNode();
        System.out.println(node);
        System.out.println(ServerNodesCache.getNode());

    }
}

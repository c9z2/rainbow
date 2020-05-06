package com.milchstrabe.rainbow.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @Author ch3ng
 * @Date 2020/4/30 13:37
 * @Version 1.0
 * @Description
 *
 **/
@Setter
@Getter
@Builder
public class Node implements Comparable<Node>{

    private String host;
    private int tcpPort;
    private int udpPort;
    private long playload;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Node node = (Node) o;
        return tcpPort == node.tcpPort &&
                Objects.equals(host, node.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, tcpPort);
    }


    @Override
    public int compareTo(Node o) {
        return this.getPlayload() >= o.getPlayload() ? 1 : -1;
    }
}

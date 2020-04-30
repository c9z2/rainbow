package com.milchstrabe.rainbow.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class Node {

    private String host;
    private int tcpPort;
    private int udpPort;
    private long playload;

}

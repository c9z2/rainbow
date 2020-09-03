package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/30 14:03
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class NodeVO {
    private String host;
    private int tcpPort;
    private int udpPort;
}

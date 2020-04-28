package com.milchstrabe.rainbow.skt.server;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/28 17:23
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class ClientServer {

    private String ip;
    private Integer port;
    private String clientType;

}

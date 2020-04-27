package com.milchstrabe.rainbow.skt.server.tcp.codc;


import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.tcp.session.Session;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/20 22:12
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class TCPRequest {

    private Data.Request request;
    private Session session;

}

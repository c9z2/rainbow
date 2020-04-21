package com.milchstrabe.rainbow.skt.server.codc;


import com.milchstrabe.rainbow.skt.server.session.Session;
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
public class Request {

    private byte firstOrder;
    private byte secondOrder;
    private byte[] data;
    private Session session;

}

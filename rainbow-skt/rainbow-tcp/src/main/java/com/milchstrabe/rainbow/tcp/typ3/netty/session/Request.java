package com.milchstrabe.rainbow.tcp.typ3.netty.session;


import com.milchstrabe.rainbow.api.netty.codc.Data;
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

    private Data.Request request;
    private Session session;

}

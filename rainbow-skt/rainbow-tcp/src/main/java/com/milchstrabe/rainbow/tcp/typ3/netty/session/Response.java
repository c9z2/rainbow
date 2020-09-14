package com.milchstrabe.rainbow.tcp.typ3.netty.session;


import com.milchstrabe.rainbow.base.server.codc.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/20 22:13
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class Response {

    private Data.Response response;

    public Response(Data.Response response){
        this.response = response;
    }

}

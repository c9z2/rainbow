package com.milchstrabe.rainbow.skt.server.tcp.codc;


import com.milchstrabe.rainbow.skt.server.codc.Data;
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
public class TCPResponse {

    private Data.Response response;

    public TCPResponse(Data.Response response){
        this.response = response;
    }

}

package com.milchstrabe.rainbow.skt.server.udp.codc;


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
public class UDPResponse {

    private Data.Response response;

}

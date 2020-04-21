package com.milchstrabe.rainbow.skt.server.codc;


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
public class Response {

    /**
     * bin
     * 1010101010101010101010101010101
     */
    private final int head = 1431655765;
    private byte firstOrder;
    private byte secondOrder;
    private byte stateCode;
    private byte[] data;

    public Response(){}

    public Response(Request request){
        this.firstOrder = request.getFirstOrder();
        this.secondOrder = request.getSecondOrder();
    }
    public Response(Request request, byte stateCode){
        this.firstOrder = request.getFirstOrder();
        this.secondOrder = request.getSecondOrder();
        this.stateCode = stateCode;
    }
}

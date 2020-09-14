package com.milchstrabe.rainbow.udp.common.util;

/**
 * @Author ch3ng
 * @Date 2020/4/21 23:21
 * @Version 1.0
 * @Description
 **/
public class ByteBufferReaderFactory {

    private ByteBufferReaderFactory(){}

    public static ByteBufferReader build(){
        return new ByteBufferReader();
    }
}

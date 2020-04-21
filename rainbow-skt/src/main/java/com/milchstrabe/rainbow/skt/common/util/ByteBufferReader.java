package com.milchstrabe.rainbow.skt.common.util;

import java.nio.charset.Charset;

/**
 * @Author ch3ng
 * @Date 2020/4/21 23:21
 * @Version 1.0
 * @Description
 **/
public class ByteBufferReader {

    protected ByteBufferReader(){}

    private int index = 0;

    public String readLong(byte[] bytes){
        byte[] data = ByteUtil.subBytes(bytes, index, 8);
        long numer = ByteUtil.bytesToLong(data);
        index += 8;
        return numer + "";
    }

    public String readString(byte[] bytes,int length){
        byte[] data = ByteUtil.subBytes(bytes, index, length);
        index += length;
        return new String(data, Charset.forName("utf-8"));
    }

    public int readInt(byte[] bytes){
        byte[] data = ByteUtil.subBytes(bytes, index, 4);
        int numer = ByteUtil.bytesToInt(data);
        index += 4;
        return numer;
    }

    public short readShort(byte[] bytes){
        byte[] data = ByteUtil.subBytes(bytes, index, 2);
        index += 2;
        return ByteUtil.bytesToShort(data);
    }

    public byte readByte(byte[] bytes){
        byte aByte = bytes[index];
        index += 1;
        return aByte;
    }

    public byte[] readBytes(byte[] bytes){
        byte[] data = ByteUtil.subBytes(bytes, index);
        index = bytes.length - 1;
        return data;
    }

}

package com.milchstrabe.rainbow.biz.message;

import com.milchstrabe.rainbow.biz.message.handler.IMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ch3ng
 * @Date 2020/9/4 15:44
 * @Version 1.0
 * @Description
 **/
public class MessageProcessorContainer {

    private static final Map<Integer, IMessageHandler> converter = new ConcurrentHashMap<>();

    private MessageProcessorContainer(){}

    public static void put(Integer type,IMessageHandler messageService){
        converter.put(type,messageService);
    }

    public static IMessageHandler get(Integer type){
        return converter.get(type);
    }
}

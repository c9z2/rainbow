package com.milchstrabe.rainbow.ws.common;

import com.milchstrabe.rainbow.ws.service.IMessageService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author ch3ng
 * @Date 2020/9/4 15:44
 * @Version 1.0
 * @Description
 **/
public class MessageProcessorContainer {

    private static final Map<Integer, IMessageService> converter = new ConcurrentHashMap<>();

    private MessageProcessorContainer(){}

    public static void put(Integer type,IMessageService messageService){
        converter.put(type,messageService);
    }

    public static IMessageService get(Integer type){
        return converter.get(type);
    }
}

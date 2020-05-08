package com.milchstrabe.rainbow.skt.server.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ch3ng
 * @Date 2020/4/20 23:25
 * @Version 1.0
 * @Description
 **/
public class InvokerHolder {

    private static Map<Integer,Map<Integer,Invoker>> invokers = new HashMap<>();

    public static Invoker getInvoker(int firstOrder,int secondOrder){
        Map<Integer, Invoker> map = invokers.get(firstOrder);
        if(map != null){
            return map.get(secondOrder);
        }
        return null;
    }

    public static void addInvoker(int firstOrder,int secondOrder,Invoker invoker){
        Map<Integer, Invoker> map = invokers.get(firstOrder);
        if(map == null){
            map = new HashMap<>();
            invokers.put(firstOrder,map);
        }
        map.put(secondOrder,invoker);

    }
}

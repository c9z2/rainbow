package com.milchstrabe.rainbow.udp.typ3.netty.scanner;

import com.milchstrabe.rainbow.udp.typ3.netty.annotion.NettyController;
import com.milchstrabe.rainbow.udp.typ3.netty.annotion.NettyMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author ch3ng
 * @Date 2020/4/21 22:30
 * @Version 1.0
 * @Description
 **/
@Component
@Slf4j
@Order(1)
public class NettyControllerScanner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void run(String... args) throws Exception {
        Class<NettyController> nettyControllerClass = NettyController.class;
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(nettyControllerClass);
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            Class<?> clazz = bean.getClass();
            NettyController annotation = clazz.getAnnotation(NettyController.class);
            int cmd1 = annotation.cmd();
            Method[] methods = clazz.getMethods();
            if(methods == null || methods.length ==0){
                continue;
            }
            for (Method method : methods) {
                NettyMapping nettyMapping = method.getAnnotation(NettyMapping.class);
                if (nettyMapping == null) {
                    continue;
                }
                int cmd2 = nettyMapping.cmd();
                if (InvokerHolder.getInvoker(cmd1, cmd2) == null) {
                    InvokerHolder.addInvoker(cmd1, cmd2, Invoker.valueOf(method, bean));
                    log.info("scan controller /{}/{}",cmd1,cmd2);
                } else {
                    throw new RuntimeException("repeat command cmd1:[ "+cmd1+" ]---cmd2:[ "+cmd2+" ]",
                            new Throwable(clazz.getName()));
                }
            }
        }
    }
}

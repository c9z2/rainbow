package com.milchstrabe.rainbow.skt.server.tcp.scanner;

import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyController;
import com.milchstrabe.rainbow.skt.server.tcp.codc.annotion.NettyMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
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
public class NettyControllerScanner implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Class<NettyController> nettyControllerClass = NettyController.class;
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(nettyControllerClass);
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            Object bean = next.getValue();
            Class<?> clazz = bean.getClass();
            NettyController annotation = clazz.getAnnotation(NettyController.class);
            byte cmd1 = annotation.cmd();
            Method[] methods = clazz.getMethods();
            if(methods == null || methods.length ==0){
                continue;
            }
            for (Method method : methods) {
                NettyMapping nettyMapping = method.getAnnotation(NettyMapping.class);
                if (nettyMapping == null) {
                    continue;
                }
                byte cmd2 = nettyMapping.cmd();
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

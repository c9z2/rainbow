package com.milchstrabe.rainbow.ws.typ3.stomp.handler;

import com.milchstrabe.rainbow.ws.service.IMessageService;
import com.milchstrabe.rainbow.ws.typ3.stomp.handler.MessageProcessorContainer;
import com.milchstrabe.rainbow.ws.typ3.stomp.handler.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author ch3ng
 * @Date 2020/9/3 17:26
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class InitMessageService implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args){
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MessageService.class);

        if(beansWithAnnotation == null || beansWithAnnotation.isEmpty()){
            return;
        }
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();

        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            MessageService annotation = value.getClass().getAnnotation(MessageService.class);
            int type = annotation.type();
            if(MessageProcessorContainer.get(type) == null){
                MessageProcessorContainer.put(type,(IMessageService) value);
            }else{
                log.info("消息转换器重复：{}",type);
            }
        }

    }
}

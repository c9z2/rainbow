package com.milchstrabe.rainbow.biz.message;

import com.milchstrabe.rainbow.biz.message.handler.IMessageHandler;
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
public class InitMessageHandler implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args){
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MessageHandler.class);

        if(beansWithAnnotation == null || beansWithAnnotation.isEmpty()){
            return;
        }
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();

        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            MessageHandler annotation = value.getClass().getAnnotation(MessageHandler.class);
            int type = annotation.type();
            if(MessageProcessorContainer.get(type) == null){
                MessageProcessorContainer.put(type,(IMessageHandler) value);
            }else{
                log.info("消息转换器重复：{}",type);
            }
        }

    }
}

package com.milchstrabe.rainbow.ws.listener;

import com.milchstrabe.rainbow.converter.ConverterContainer;
import com.milchstrabe.rainbow.converter.IMessageConverter;
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
public class InitMessageConverter implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Converter.class);

        if(beansWithAnnotation == null || beansWithAnnotation.isEmpty()){
            return;
        }
        Set<Map.Entry<String, Object>> entries = beansWithAnnotation.entrySet();

        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            Converter annotation = value.getClass().getAnnotation(Converter.class);
            int type = annotation.type();
            if(ConverterContainer.get(type) == null){
                ConverterContainer.put(type,(IMessageConverter) value);
            }else{
                log.info("消息转换器重复：{}",type);
            }


        }

    }
}

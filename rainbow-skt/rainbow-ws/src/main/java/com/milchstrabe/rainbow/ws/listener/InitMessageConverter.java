package com.milchstrabe.rainbow.ws.listener;

import com.milchstrabe.rainbow.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author ch3ng
 * @Date 2020/9/3 17:26
 * @Version 1.0
 * @Description
 **/
@Component
public class InitMessageConverter implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Converter.class);

    }
}

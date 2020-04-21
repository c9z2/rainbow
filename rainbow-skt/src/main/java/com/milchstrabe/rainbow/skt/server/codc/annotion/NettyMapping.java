package com.milchstrabe.rainbow.skt.server.codc.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author ch3ng
 * @Date 2020/4/20 23:13
 * @Version 1.0
 * @Description
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyMapping {

    byte cmd();
}

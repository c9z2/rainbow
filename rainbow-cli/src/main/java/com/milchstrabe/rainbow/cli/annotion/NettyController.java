package com.milchstrabe.rainbow.cli.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author ch3ng
 * @Date 2020/4/21 23:10
 * @Version 1.0
 * @Description
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyController {

    int cmd();
}

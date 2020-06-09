package com.milchstrabe.rainbow.ws.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @Author ch3ng
 * @Date 2020/06/05 21:54
 * @Version 1.0
 * @Description import fastdfs config
 **/
@Configuration
@ComponentScan
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FdfsClientConfig {
    // auto import
}
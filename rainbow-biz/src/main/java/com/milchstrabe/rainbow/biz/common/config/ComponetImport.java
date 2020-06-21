package com.milchstrabe.rainbow.biz.common.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @Author ch3ng
 * @Date 2020/06/05 21:54
 * @Version 1.0
 * @Description import fastdfs config
 **/
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ComponetImport {

}
package com.milchstrabe.rainbow.biz.common.conf;

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
/**
 * 导入FastDFS-Client组件
 *
 * @author tobato
 *
 */
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ComponetImport {

    public ComponetImport(){
        System.out.println("123");
    }
    // 导入依赖组件
}
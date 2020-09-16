package com.milchstrabe.rainbow.biz.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author ch3ng
 * @Date 2020/05/31 21:54
 * @Version 1.0
 * @Description
 **/
@Configuration
@EnableAsync
@Slf4j
public class TaskPoolConfig {

    @Value("${thread.pool.corePoolSize}")
    private Integer corePoolSize;
    @Value("${thread.pool.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${thread.pool.queueCapacity}")
    private Integer queueCapacity;

    @Bean("asyncExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("asyncExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }


}

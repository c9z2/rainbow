package com.milchstrabe.rainbow.biz.common.config;

import com.milchstrabe.rainbow.biz.common.UserArgumentsResolver;
import com.milchstrabe.rainbow.biz.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:21
 * @Version 1.0
 * @Description
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private UserArgumentsResolver userArgumentsResolver;

    /**
     * /sys/v1/signIn
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/**/signIn","/**/signUp","/error");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentsResolver);
    }
}

package com.milchstrabe.rainbow.biz.common;

import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:20
 * @Version 1.0
 * @Description 
 **/
@Component
@Slf4j
public class UserArgumentsResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private IUserMappper userMappper;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest nativeRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String userId = nativeRequest.getAttribute("userId").toString();
        log.info(userId);
        User userInToken = userMappper.findUserByUserId(userId);
        RequestUser requestUser = RequestUser.builder().build();
        BeanUtils.copyProperties(userInToken,requestUser);
        return requestUser;
    }
}

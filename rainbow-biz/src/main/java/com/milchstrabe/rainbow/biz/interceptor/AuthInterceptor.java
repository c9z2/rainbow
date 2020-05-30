package com.milchstrabe.rainbow.biz.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:14
 * @Version 1.0
 * @Description
 **/
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthException {
        String authorization = request.getHeader("Authorization");
        log.info(authorization);
        if(!StringUtils.hasLength(authorization)){
            throw new AuthException("miss auth!");
        }
        String[] tokens = authorization.split(" ");
        if(tokens.length<2){
            throw new AuthException("miss auth!");
        }
        String token = tokens[1];
        DecodedJWT decode = null;
        try {
            decode = JWT.decode(token);
        }catch (JWTDecodeException exception){
            log.error(exception.getMessage());
            throw new AuthException("berarer token err!");
        }

        Claim user = decode.getClaim("userId");
        String userId = user.asString();
        request.setAttribute("userId",userId);
        return true;
    }
}

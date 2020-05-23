package com.milchstrabe.rainbow.ws.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.server.domain.vo.SiginVO;
import com.milchstrabe.rainbow.ws.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/5/22 22:33
 * @Version 1.0
 * @Description sign in websocket server
 **/
@Slf4j
@RestController
public class SignInController {


    @PostMapping("/sys/signIn")
    public Result signIn(SiginVO siginVO,
                         HttpSession session) throws AuthException, ParamMissException {
        Optional.ofNullable(siginVO).orElseThrow(()->{
            return new ParamMissException("param miss");
        });

        DecodedJWT decode = null;
        try {
            String token = siginVO.getToken();
            decode = JWT.decode(token);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new AuthException(e.getMessage());
        }

        String userId = decode.getClaim("userId").asString();
        String username = decode.getClaim("username").asString();

        UCI uci = UCI.builder().username(username).uid(userId).cid(siginVO.getCid()).build();

        session.setAttribute(Constant.USER_IN_SESSION,uci);

        return ResultBuilder.success();

    }
}

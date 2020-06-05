package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.vo.SignInVO;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/4/29 15:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/sys")
public class SignInController {


    @Autowired
    private ISystemService systemService;


    @PostMapping(path = APIVersion.V_1 + "/signIn")
    public Result<String> login(@RequestBody SignInVO signInVO) throws LogicException, ParamMissException {
        Optional.ofNullable(signInVO).orElseThrow(()->new ParamMissException("miss params"));
        Optional.ofNullable(signInVO.getUsername()).orElseThrow(()->new ParamMissException("username or password error"));
        Optional.ofNullable(signInVO.getPasswd()).orElseThrow(()->new ParamMissException("username or password error"));
        try {
            UserDTO userDTO = UserDTO.builder()
                    .username(signInVO.getUsername())
                    .passwd(signInVO.getPasswd())
                    .build();
            String jwt = systemService.signIn(userDTO);
            return ResultBuilder.success(jwt);
        } catch (LogicException e) {
           log.error(e.getMessage());
           throw new LogicException(50001,e.getMessage());
        }
    }
}

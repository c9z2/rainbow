package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.vo.RegisterVO;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/4/29 15:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/sys")
public class RegisterController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ISystemService systemService;
    private static final String SIGN_UP_KEY = "rainbow:sign:up:";

    @PutMapping(path = APIVersion.V_1 + "/signUp")
    public Result<String> register(@RequestBody @Validated RegisterVO registerVO) throws LogicException, ParamMissException {

        String codeInRedis = redisTemplate.opsForValue().get(SIGN_UP_KEY + registerVO.getEmail());
        if(codeInRedis == null){
            return ResultBuilder.fail("验证码失效");
        }
        if(!registerVO.getCode().equals(codeInRedis)){
            return ResultBuilder.fail("验证码错误");
        }

        String userId = IdUtil.objectId();
        UserPropertyDTO userPropertyDTO = UserPropertyDTO.builder()
                .nickname(registerVO.getNickname())
                .email(registerVO.getEmail())
                .build();

        UserDTO userDTO = UserDTO.builder()
                .passwd(MD5.create().digestHex(registerVO.getPasswd()))
                .username(registerVO.getUsername())
                .property(userPropertyDTO)
                .userId(userId)
                .status((short)1)
                .build();

        systemService.register(userDTO);

        return ResultBuilder.success();
    }


}

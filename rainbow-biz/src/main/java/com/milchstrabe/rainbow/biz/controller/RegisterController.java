package com.milchstrabe.rainbow.biz.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

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
    private ISystemService systemService;


    @PutMapping(path = APIVersion.V_1 + "/signUp")
    public Result<String> register(@RequestBody RegisterVO registerVO) throws LogicException, ParamMissException {
        Optional.ofNullable(registerVO).orElseThrow(()-> new ParamMissException("miss params"));
        Optional.ofNullable(registerVO.getNickname()).orElseThrow(()-> new ParamMissException("miss nickname"));
        Optional.ofNullable(registerVO.getPasswd()).orElseThrow(()-> new ParamMissException("miss password"));
        Optional.ofNullable(registerVO.getUsername()).orElseThrow(()-> new ParamMissException("miss username"));

        String userId = UUID.randomUUID().toString().replace("-","");
        UserPropertyDTO userPropertyDTO = UserPropertyDTO.builder().nickname(registerVO.getNickname()).build();
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

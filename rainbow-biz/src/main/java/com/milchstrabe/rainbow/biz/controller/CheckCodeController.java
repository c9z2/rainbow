package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.crypto.digest.MD5;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.vo.RegisterVO;
import com.milchstrabe.rainbow.biz.domain.vo.SendCheckCodeVO;
import com.milchstrabe.rainbow.biz.domain.vo.SendResetPwdCheckCodeVO;
import com.milchstrabe.rainbow.biz.service.ICheckCodeService;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class CheckCodeController {


    @Autowired
    private ICheckCodeService checkCodeService;


    @PutMapping(path = APIVersion.V_1 + "/checkcode/sign/up")
    public Result<String> registerCheckCode(@RequestBody @Validated SendCheckCodeVO vo) throws LogicException {

        String email = vo.getEmail();
        checkCodeService.sendSignUpCheckcode(email);

        return ResultBuilder.success();

    }


    @PutMapping(path = APIVersion.V_1 + "/checkcode/reset/pwd")
    public Result<String> resetPasswdCheckCode(@RequestBody @Validated SendResetPwdCheckCodeVO vo) throws LogicException {

        String email = vo.getEmail();
        String username = vo.getUsername();
        checkCodeService.sendResetCheckCode(username,email);

        return ResultBuilder.success();

    }


}

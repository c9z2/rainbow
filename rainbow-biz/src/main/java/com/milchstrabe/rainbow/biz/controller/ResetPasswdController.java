package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.dto.ResetPasswdDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.vo.ResetPasswdVO;
import com.milchstrabe.rainbow.biz.domain.vo.SignInVO;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
public class ResetPasswdController {


    @Autowired
    private ISystemService systemService;


    @PostMapping(path = APIVersion.V_1 + "/reset/passwd")
    public Result<String> resetPasswd(@RequestBody @Validated ResetPasswdVO vo) throws LogicException, ParamMissException {
        ResetPasswdDTO dto = new ResetPasswdDTO();
        BeanUtil.copyProperties(vo,dto);

        systemService.resetPasswd(dto);

        return ResultBuilder.success();
    }
}

package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/5/11 11:52
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/cli")
public class CLIController {

    @Autowired
    private ISystemService systemService;

    @PostMapping(path = "/fingerprint")
    public Result<String> fingerprint(@CurrentUser User user,String ctype) throws LogicException {

        String fingerprint = systemService.fingerprint(user, ctype);

        return ResultBuilder.success(fingerprint);
    }
}

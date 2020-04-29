package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.exception.LogicException;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/4/29 15:56
 * @Version 1.0
 * @Description
 **/
@RestController
@RequestMapping("/sys")
public class SignInController {


    @Autowired
    private ISystemService systemService;


    @PostMapping(path = "/signIn")
    public Result<String> login(String username, String password){
        try {
            String jwt = systemService.signIn(username, password);
            return ResultBuilder.success(jwt);
        } catch (LogicException e) {
            e.printStackTrace();
            return ResultBuilder.exception(e.getMessage());
        }
    }
}

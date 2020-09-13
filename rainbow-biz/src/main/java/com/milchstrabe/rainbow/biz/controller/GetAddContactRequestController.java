package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.server.domain.po.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/4/29 15:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/contact")
public class GetAddContactRequestController {


    @Autowired
    private IContactService contactService;


    @GetMapping(path = APIVersion.V_1 + "/add/request/list")
    public Result<List<Message>> resetPasswd(@CurrentUser RequestUser user) {
        List<Message> result= contactService.getAddContactRequest(user.getUserId());
        return ResultBuilder.success(result);
    }
}

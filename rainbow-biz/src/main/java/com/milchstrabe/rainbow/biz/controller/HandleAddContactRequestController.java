package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.vo.HandleAddContactVO;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
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
@RequestMapping("/contact")
public class HandleAddContactRequestController {


    @Autowired
    private IContactService contactService;


    @PostMapping(path = APIVersion.V_1 + "/handle/request")
    public Result addContactRequest(@CurrentUser RequestUser user,
                                    @RequestBody @Validated HandleAddContactVO vo) throws LogicException {

        contactService.handleAddContact(user.getUserId(),vo.getSender(),vo.getHandle());
        return ResultBuilder.success();
    }
}

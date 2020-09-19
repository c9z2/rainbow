package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/6/5 21:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/contact")
public class DeleteContactController {

    @Autowired
    private IContactService contactService;


    /**
     *
     * @param user
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @DeleteMapping(path = APIVersion.V_1 + "/del/{contactId}")
    public Result<String> property(@CurrentUser RequestUser user,
                                 @PathVariable("contactId") String contactId) throws LogicException {
        contactService.deleteContact(user.getUserId(),contactId);
        return ResultBuilder.success();
    }
}

package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/6/5 21:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class ModifiedUserAvatarController {

    @Autowired
    private IUserService userService;

    /**
     *
     * @param user
     * @param file
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @PostMapping(path = APIVersion.V_1 + "/avatar")
    public Result<String> avatar(@CurrentUser RequestUser user, MultipartFile file) throws LogicException, ParamMissException {
        Optional.ofNullable(file).orElseThrow(()->new ParamMissException("miss avatar"));
        String s = userService.modifiedUserAvatar(user.getUserId(), file);
        return ResultBuilder.success(s);
    }
}

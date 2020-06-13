package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author ch3ng
 * @Date 2020/6/13 16:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class GetCurrentUserInfoController {

    @Autowired
    private IUserService userService;

    /**
     *
     * @param user
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @GetMapping(path = APIVersion.V_1 + "/property")
    public Result<String> currentUserInfo(@CurrentUser RequestUser user) throws LogicException, ParamMissException, IOException {
        UserDTO userDTO = BeanUtils.map(user, UserDTO.class);
        UserPropertyDTO userProperty = userService.getUserProperty(userDTO);
        return ResultBuilder.success(userProperty);
    }
}

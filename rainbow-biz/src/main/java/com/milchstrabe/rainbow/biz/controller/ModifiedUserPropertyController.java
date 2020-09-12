package com.milchstrabe.rainbow.biz.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.vo.ModifiedUserPropertyVO;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
public class ModifiedUserPropertyController {


    @Autowired
    private IUserService userService;


    /**
     *
     * @param user
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @PostMapping(path = APIVersion.V_1 + "/property")
    public Result<String> property(@CurrentUser RequestUser user,
                                 @RequestBody ModifiedUserPropertyVO userPropertyVO) throws LogicException, ParamMissException, IOException {
        //TODO
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
        UserPropertyDTO userPropertyDTO = UserPropertyDTO.builder().build();
        BeanUtils.copyProperties(userPropertyVO,userPropertyDTO);
        userDTO.setProperty(userPropertyDTO);
        userService.modifiedUserAvatar(userDTO);
        return null;
    }
}

package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.vo.ContactVO;
import com.milchstrabe.rainbow.biz.domain.vo.SearchUserVO;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/5/12 17:09
 * @Version 1.0
 * @Description
 **/
@RestController
@RequestMapping("/user")
public class SearchContactsController {

    @Autowired
    private IUserService userService;

    @GetMapping(path = APIVersion.V_1 + "/search/{content}")
    public Result<ContactVO> search(@PathVariable("content")String content) {


        UserDTO userDTO = UserDTO
                .builder()
                .status((short) 1)
                .username(content)
                .property(UserPropertyDTO.builder().email(content).build())
                .build();

        UserDTO userResult = userService.searchUser(userDTO);
        if(userResult == null){
            return ResultBuilder.success();
        }

        SearchUserVO searchUserVO = BeanUtils.map(userResult.getProperty(), SearchUserVO.class);
        searchUserVO.setUserId(userResult.getUserId());
        searchUserVO.setUsername(userResult.getUsername());

        return ResultBuilder.success(searchUserVO);
    }


}

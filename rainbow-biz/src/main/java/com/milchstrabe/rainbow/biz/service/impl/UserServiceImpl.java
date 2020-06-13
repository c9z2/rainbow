package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/6/10 18:44
 * @Version 1.0
 * @Description
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMappper userMappper;

    @Override
    public void modifiedUserAvatar(UserDTO userDTO) throws LogicException {
        User user = BeanUtils.map(userDTO, User.class);
        boolean isSuccess = userMappper.updateUserPropertyByUserId(user);
        if(!isSuccess){
            throw new LogicException(5000,"modified user info fail");
        }
    }

    @Override
    public UserPropertyDTO getUserProperty(UserDTO userDTO) throws LogicException {
        UserProperty userProperty = userMappper.findUserPropertyByUserId(userDTO.getUserId());
        Optional.ofNullable(userProperty).orElseThrow(()-> new LogicException(5000,"user property not found"));
        UserPropertyDTO userPropertyDTO = BeanUtils.map(userProperty, UserPropertyDTO.class);
        return userPropertyDTO;
    }

    @Override
    public UserDTO findUserByUsernameAndStatus(UserDTO userDTO) {
        User user = userMappper.findUserAndPropertyByUsernameAndStatus(userDTO.getUsername(), userDTO.getStatus());
        if(user == null){
            return null;
        }
        return BeanUtils.map(user, UserDTO.class);
    }
}

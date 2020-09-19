package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserPropertyMapper;
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

    @Autowired
    private IUserPropertyMapper userPropertyMapper;

    @Override
    public void modifiedUserAvatar(UserDTO userDTO) throws LogicException {
        User user = BeanUtils.map(userDTO, User.class);
        boolean isSuccess = userPropertyMapper.updateUserAvatar(user);
        if(!isSuccess){
            throw new LogicException(500,"修改头像失败");
        }
    }

    @Override
    public void modifiedUserProperty(UserDTO dto) throws LogicException {
        User user = BeanUtils.map(dto, User.class);
        boolean isSuccess = userPropertyMapper.updateUserPropertyByUserId(user);
        if(!isSuccess){
            throw new LogicException(500,"修改信息失败");
        }
    }

    @Override
    public UserPropertyDTO getUserProperty(UserDTO userDTO) throws LogicException {
        UserProperty userProperty = userPropertyMapper.findUserPropertyByUserId(userDTO.getUserId());
        Optional.ofNullable(userProperty).orElseThrow(()-> new LogicException(500,"user property not found"));
        UserPropertyDTO userPropertyDTO = BeanUtils.map(userProperty, UserPropertyDTO.class);
        return userPropertyDTO;
    }


    @Override
    public UserDTO searchUser(UserDTO dto) {
        User user = userMappper.searchUser(dto.getUsername(), dto.getStatus(), dto.getProperty().getEmail());
        if(user == null){
            return null;
        }
        return BeanUtils.map(user, UserDTO.class);
    }


}

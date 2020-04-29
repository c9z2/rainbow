package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUserMappper {

    boolean addUser(@Param("user") User user);

    User findUserByUsername(@Param("username") String username);

    User findUserByUserId(@Param("id") String id);

}

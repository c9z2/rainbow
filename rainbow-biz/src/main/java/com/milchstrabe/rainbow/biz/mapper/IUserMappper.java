package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUserMappper {

    boolean addUser(@Param("user") User user);



    User findUserByUsernameAndStatus(@Param("username") String username,@Param("status") Short status);

    User findUserByUsername(@Param("username") String username);

    User findUserByUserId(@Param("id") String id);

    boolean updatePasswordByUsername(@Param("username") String username,@Param("passwd") String password);

    User findUserAndPropertyByUsernameAndStatus(@Param("username") String username,@Param("status") Short status);

}

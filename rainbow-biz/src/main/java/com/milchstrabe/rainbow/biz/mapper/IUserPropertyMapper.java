package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author ch3ng
 * @Date 2020/9/3 11:00
 * @Version 1.0
 * @Description
 **/
@Mapper
public interface IUserPropertyMapper {

    boolean addUserProperty(@Param("user") User user);

    boolean updateUserPropertyByUserId(@Param("user") User user);

    UserProperty findUserPropertyByUserId(@Param("userId") String userId);

    UserProperty findUserPropertyByEmail(@Param("email") String email);
}

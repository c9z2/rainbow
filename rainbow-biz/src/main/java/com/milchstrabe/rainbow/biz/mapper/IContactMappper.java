package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IContactMappper {

    List<Contact> list(@Param("uid")String uid);


}

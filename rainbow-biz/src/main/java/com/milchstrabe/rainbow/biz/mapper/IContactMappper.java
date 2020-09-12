package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IContactMappper {

    List<Contact> list(@Param("uid")String uid);

    Contact findContactDetail(@Param("userId") String userId, @Param("contactId") String contactId);

    boolean modifiedContactRemark(@Param("userId") String userId, @Param("contactId") String contactId,@Param("remark") String remark);


}

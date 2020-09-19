package com.milchstrabe.rainbow.biz.mapper;

import com.milchstrabe.rainbow.biz.domain.po.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IContactMappper {

    List<Contact> list(@Param("uid")String uid);

    Contact findContactDetail(@Param("userId") String userId, @Param("contactId") String contactId);

    boolean modifiedContactRemark(@Param("userId") String userId, @Param("contactId") String contactId,@Param("remark") String remark);

    boolean addContact(@Param("sender") String sender,
                       @Param("receiver") String receiver,
                       @Param("senderNickname") String senderNickname,
                       @Param("receiverNickname") String receiverNickname,
                       @Param("createTime") Long createTime);

    List<Map<String,String>> findContactRelationship(@Param("userId") String userId, @Param("contactId") String contactId);

    boolean deleteContact(@Param("userId") String userId, @Param("contactId") String contactId);


}

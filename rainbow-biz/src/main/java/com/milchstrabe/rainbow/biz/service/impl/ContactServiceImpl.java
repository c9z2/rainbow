package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.biz.domain.vo.GetContactDetailVO;
import com.milchstrabe.rainbow.biz.mapper.IContactMappper;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/5/12 20:36
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    private IContactMappper contactMappper;

    @Override
    public List<Contact> list(String uid) {
        List<Contact> list = contactMappper.list(uid);
        return list;
    }

    @Override
    public GetContactDetailDTO findContactDetail(String userId, String contactId) {
        Contact contact = contactMappper.findContactDetail(userId, contactId);
        GetContactDetailDTO dto = GetContactDetailDTO.builder()
                .age(contact.getUser().getProperty().getAge())
                .avatar(contact.getUser().getProperty().getAvatar())
                .email(contact.getUser().getProperty().getEmail())
                .gender(contact.getUser().getProperty().getGender())
                .nickname(contact.getUser().getProperty().getNickname())
                .phone(contact.getUser().getProperty().getPhone())
                .remark(contact.getRemark())
                .signature(contact.getUser().getProperty().getSignature())
                .userId(contact.getUser().getUserId())
                .username(contact.getUser().getUsername())
                .createTime(contact.getCreateTime())
                .build();

        return dto;
    }

    @Override
    public void modifiedContactRemark(ModifiedContactRemarkDTO dto) throws LogicException {
        boolean b = contactMappper.modifiedContactRemark(dto.getUserId(), dto.getContactId(), dto.getRemark());
        if(!b){
            throw new LogicException(5000,"改备注失败");
        }
    }
}

package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.exception.LogicException;

import java.util.List;

public interface IContactService {

    List<Contact> list(String uid);

    GetContactDetailDTO findContactDetail(String userId, String contactId);

    void modifiedContactRemark(ModifiedContactRemarkDTO dto) throws LogicException;

}

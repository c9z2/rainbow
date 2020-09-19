package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.dto.AddContactMessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.MessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.po.Message;

import java.util.List;

public interface IContactService {

    List<Contact> list(String uid);

    GetContactDetailDTO findContactDetail(String userId, String contactId);

    void modifiedContactRemark(ModifiedContactRemarkDTO dto) throws LogicException;

    List<Message> getAddContactRequest(String userId);

    GetContactDetailDTO addContactMessage(MessageDTO<AddContactMessageDTO> message) throws LogicException;

    void handleAddContact(String userId,String sender,Short handle) throws LogicException;

    void deleteContact(String userId,String contactId) throws LogicException;

}

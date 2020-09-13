package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.domain.dto.AddContactMessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.MessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.biz.mapper.IContactMappper;
import com.milchstrabe.rainbow.biz.repository.MessageRepository;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private MessageRepository messageRepository;



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

    @Override
    public List<Message> getAddContactRequest(String userId) {
        List<Message> messagess = messageRepository.getAddContactMessage(userId);
        return messagess;
    }

    @Override
    public void addContactMessage(MessageDTO<AddContactMessageDTO> message) throws LogicException {
        Message<AddContactMessage> messageInDatabase = messageRepository.getAddContactMessage(message.getSender(), message.getReceiver());

        if(messageInDatabase != null && messageInDatabase.getContent().getStatus() == 0){
            throw new LogicException(300,"请求已发送，请稍后...");
        }
        if(messageInDatabase != null && messageInDatabase.getContent().getStatus() == 1){
            throw new LogicException(300,"你们已经是好友了");
        }

        AddContactMessage addContactMessage = new AddContactMessage();
        BeanUtils.copyProperties(message.getContent(), addContactMessage);

        Message<AddContactMessage> po = new Message<>();
        BeanUtils.copyProperties(message, po);
        po.setContent(addContactMessage);

        boolean isSuccess = messageRepository.addContactMessage(po);
        if(!isSuccess){
            throw new LogicException(500,"发送请求失败");
        }
    }

    @Override
    public void handleAddContact(String userId, String sender, Short handle) throws LogicException {
        boolean isSuccess = messageRepository.handleAddContact(userId, sender, handle);
        if(!isSuccess){
            throw new LogicException(500,"处理失败");
        }
        if(1 == handle){
            //accept
            //logic relationship
            Message<AddContactMessage> addContactMessage = messageRepository.getAddContactMessage(sender, userId);
            AddContactMessage content = addContactMessage.getContent();
            boolean isOk = contactMappper.addContact(addContactMessage.getSender(),
                    addContactMessage.getReceiver(),
                    content.getNickname(),
                    content.getReceiverNickname(),
                    System.currentTimeMillis());

            if(!isOk){
                throw new LogicException(500,"添加好友失败");
            }


        }

    }
}

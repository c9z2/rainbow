package com.milchstrabe.rainbow.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.api.typ3.grpc.Msg;
import com.milchstrabe.rainbow.biz.domain.dto.AddContactMessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.GetContactDetailDTO;
import com.milchstrabe.rainbow.biz.domain.dto.MessageDTO;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.biz.mapper.IContactMappper;
import com.milchstrabe.rainbow.biz.repository.ClientServerRepository;
import com.milchstrabe.rainbow.biz.repository.ContactRequestMessageRepository;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.biz.typ3.grpc.GRPCClient;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private ContactRequestMessageRepository messageRepository;

    @Autowired
    private GRPCClient grpcClient;

    @Autowired
    private ClientServerRepository clientServerRepository;



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
    public GetContactDetailDTO addContactMessage(MessageDTO<AddContactMessageDTO> message) throws LogicException {
        String sender = message.getSender();
        String receiver = message.getReceiver();

        List<Map<String,String>> contactRelationship = contactMappper.findContactRelationship(sender, receiver);
        if(contactRelationship.size() == 2){
            throw new LogicException(300,"你们已经是好友了");
        }
        if(contactRelationship.size() == 1){
            Map<String,String> map = contactRelationship.get(0);
            if(map.get("contactId").equals(sender)){
                boolean isSuccess = contactMappper.addContact(message.getSender(),
                        message.getReceiver(),
                        message.getContent().getNickname(),
                        message.getContent().getReceiverNickname(), System.currentTimeMillis());
                if(!isSuccess){
                    throw new LogicException(500,"添加好友异常");
                }else{
                   return findContactDetail(sender,receiver);
                }
            }
        }

        Message<AddContactMessage> messageInDatabase = messageRepository.getAddContactMessage(message.getSender(), message.getReceiver());

        if(messageInDatabase != null){
            if(messageInDatabase.getContent().getStatus() == 0){
                throw new LogicException(300,"请求已发送，请稍后...");
            }else{
                messageInDatabase.getContent().setStatus((short)0);
                message.getContent().setAvatar(message.getContent().getAvatar());
                message.getContent().setNickname(message.getContent().getNickname());
                message.getContent().setNote(message.getContent().getNote());
                message.getContent().setReceiverNickname(message.getContent().getReceiverNickname());
                message.getContent().setUsername(message.getContent().getUsername());
                boolean isSuccess = messageRepository.updateAddContactContent(messageInDatabase);
                if(!isSuccess){
                    throw new LogicException(500,"发送请求失败");
                }

            }

        }else{
            AddContactMessage addContactMessage = new AddContactMessage();
            BeanUtils.copyProperties(message.getContent(), addContactMessage);

            messageInDatabase = new Message<>();
            BeanUtils.copyProperties(message, messageInDatabase);
            messageInDatabase.setContent(addContactMessage);
            boolean isSuccess = messageRepository.addContactMessage(messageInDatabase);
            if(!isSuccess){
                throw new LogicException(500,"发送请求失败");
            }
        }

        Msg.MsgRequest msgRequest = Msg.MsgRequest.newBuilder()
                .setMsgId(messageInDatabase.getId())
                .setMsgType(messageInDatabase.getMsgType())
                .setContent(JSON.toJSONString(messageInDatabase.getContent()))
                .setSender(messageInDatabase.getSender())
                .setReceiver(messageInDatabase.getReceiver())
                .setDate(messageInDatabase.getDate())
                .build();

        Set<ClientServer> css = clientServerRepository.findCSByUid(messageInDatabase.getReceiver());
        Iterator<ClientServer> iterator = css.iterator();
        while (iterator.hasNext()){
            ClientServer cs = iterator.next();
            grpcClient.sender(cs.getHost(),cs.getPort(),msgRequest);
        }
        return null;
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

    @Override
    public void deleteContact(String userId, String contactId) throws LogicException {
        Contact contact = contactMappper.findContactDetail(userId, contactId);
        Optional.ofNullable(contact).orElseThrow(()->{
           return new LogicException(300,"非法操作");
        });
        boolean isSuccess = contactMappper.deleteContact(userId, contactId);
        if(!isSuccess){
            throw new LogicException(500,"解除好友关系失败");
        }
    }
}

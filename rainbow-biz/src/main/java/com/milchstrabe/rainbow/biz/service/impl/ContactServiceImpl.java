package com.milchstrabe.rainbow.biz.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.api.typ3.grpc.Msg;
import com.milchstrabe.rainbow.biz.common.util.SnowFlake;
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
import com.milchstrabe.rainbow.server.domain.dto.AcceptAddContactMessageDTO;
import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.ContactBrief;
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
    public List<Map<String, String>> findContactRelationship(String userId, String contactId) {
        List<Map<String, String>> contactRelationship = contactMappper.findContactRelationship(userId, contactId);
        return contactRelationship;
    }

    @Override
    public GetContactDetailDTO findContactDetail(String userId, String contactId) throws LogicException {
        Contact contact = contactMappper.findContactDetail(userId, contactId);
        Optional.ofNullable(contact).orElseThrow(() -> {
            return new LogicException(300,"好友不存在");
        });
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
                //对方还持有当前发送者的好友关系
                boolean isSuccess = contactMappper.addContact(message.getSender(),
                        message.getReceiver(),
                        message.getContent().getSender().getNickname(),
                        message.getContent().getReceiver().getNickname(), System.currentTimeMillis());
                if(!isSuccess){
                    throw new LogicException(500,"添加好友异常");
                }else{
                    return GetContactDetailDTO.builder()
                            .username(message.getContent().getReceiver().getUsername())
                            .remark(message.getContent().getReceiver().getNickname())
                            .avatar(message.getContent().getReceiver().getAvatar())
                            .userId(message.getReceiver())
                            .nickname(message.getContent().getReceiver().getNickname())
                            .build();
                }
            }
        }

        Message<AddContactMessage> messageInDatabase = messageRepository.getAddContactMessage(message.getSender(), message.getReceiver());

        if(messageInDatabase != null){
            if(messageInDatabase.getContent().getStatus() == 0){
                throw new LogicException(300,"请求已发送，请稍后...");
            }else{
                //需要当前双方最新的个人信息
                messageInDatabase.getContent().setStatus((short)0);
                //warp senderContactBrief
                ContactBrief senderContactBrief = new ContactBrief();
                BeanUtils.copyProperties(message.getContent().getSender(), senderContactBrief);
                //warp receiverContactBrief
                ContactBrief receiverContactBrief = new ContactBrief();
                BeanUtils.copyProperties(message.getContent().getReceiver(), receiverContactBrief);

                messageInDatabase.getContent().setSender(senderContactBrief);
                messageInDatabase.getContent().setReceiver(receiverContactBrief);
                messageInDatabase.getContent().setNote(message.getContent().getNote());
                boolean isSuccess = messageRepository.updateAddContactContent(messageInDatabase);
                if(!isSuccess){
                    throw new LogicException(500,"发送请求失败");
                }

            }

        }else{
            //warp senderContactBrief
            ContactBrief senderContactBrief = new ContactBrief();
            BeanUtils.copyProperties(message.getContent().getSender(), senderContactBrief);
            //warp receiverContactBrief
            ContactBrief receiverContactBrief = new ContactBrief();
            BeanUtils.copyProperties(message.getContent().getReceiver(), receiverContactBrief);

            AddContactMessage addContactMessage = new AddContactMessage();
            addContactMessage.setSender(senderContactBrief);
            addContactMessage.setReceiver(receiverContactBrief);
            addContactMessage.setStatus(message.getContent().getStatus());
            addContactMessage.setNote(message.getContent().getNote());

            messageInDatabase = new Message<>();
            BeanUtils.copyProperties(message, messageInDatabase);
            messageInDatabase.setContent(addContactMessage);
            boolean isSuccess = messageRepository.addContactMessage(messageInDatabase);
            if(!isSuccess){
                throw new LogicException(500,"发送请求失败");
            }
        }
        Msg.MsgRequest msgRequest = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            msgRequest = Msg.MsgRequest.newBuilder()
                    .setMsgId(messageInDatabase.getId())
                    .setMsgType(messageInDatabase.getMsgType())
                    .setContent(objectMapper.writeValueAsString(messageInDatabase.getContent()))
                    .setSender(messageInDatabase.getSender())
                    .setReceiver(messageInDatabase.getReceiver())
                    .setDate(messageInDatabase.getDate())
                    .build();
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }


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
                    content.getSender().getNickname(),
                    content.getReceiver().getNickname(),
                    System.currentTimeMillis());

            if(!isOk){
                throw new LogicException(500,"添加好友失败");
            }

            AcceptAddContactMessageDTO messageDetail = AcceptAddContactMessageDTO.builder()
                    .avatar(content.getReceiver().getAvatar())
                    .createTime(System.currentTimeMillis())
                    .remark(content.getReceiver().getNickname())
                    .userId(addContactMessage.getReceiver())
                    .username(content.getReceiver().getUsername())
                    .build();
            Msg.MsgRequest msgRequest =null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                msgRequest = Msg.MsgRequest.newBuilder()
                        .setMsgId(SnowFlake.id())
                        .setMsgType(11)
                        .setContent(objectMapper.writeValueAsString(messageDetail))
                        .setSender(userId)
                        .setReceiver(sender)
                        .setDate(System.currentTimeMillis())
                        .build();
            }catch (JsonProcessingException e){
                log.error(e.getMessage());
                throw new LogicException(500,e.getMessage());
            }

            Set<ClientServer> css = clientServerRepository.findCSByUid(sender);
            Iterator<ClientServer> iterator = css.iterator();
            while (iterator.hasNext()){
                ClientServer cs = iterator.next();
                grpcClient.sender(cs.getHost(),cs.getPort(),msgRequest);
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

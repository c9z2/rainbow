package com.milchstrabe.rainbow.ws.service.impl;

import com.google.gson.Gson;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.base.server.annotion.MessageService;
import com.milchstrabe.rainbow.base.server.typ3.grpc.Msg;
import com.milchstrabe.rainbow.server.domain.dto.Message;
import com.milchstrabe.rainbow.ws.repository.ClientServerRepository;
import com.milchstrabe.rainbow.ws.server.typ3.grpc.GRPCClient;
import com.milchstrabe.rainbow.ws.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author ch3ng
 * @Date 2020/9/4 19:24
 * @Version 1.0
 * @Description
 **/
@Slf4j
@MessageService(type = 10)
public class AddContactMessageServiceImpl implements IMessageService {

    private static final String COLLECTION_NAME = "add_contact_messages";

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ClientServerRepository clientServerRepository;

    @Autowired
    private GRPCClient grpcClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void doMessage(Message message) {

        String receiver = message.getReceiver();
        Gson gson = new Gson();
        String json = gson.toJson(message);

        mongoTemplate.save()

        simpMessageSendingOperations.convertAndSendToUser(receiver, "/message", json);

        List<String> ucis = new ArrayList<>();
        Msg.MsgRequest msgRequest = Msg.MsgRequest.newBuilder()
                .setMsgId(message.getId())
                .setMsgType(message.getMsgType())
                .setContent(message.getContent())
                .setSender(message.getSender())
                .setReceiver(message.getReceiver())
                .setDate(message.getDate())
                .build();

        //current server node sessions
        Set<ClientServer> css = clientServerRepository.findCSByUid(receiver);
        Iterator<ClientServer> iterator = css.iterator();
        while (iterator.hasNext()){
            //TODO gRPC fail
            ClientServer cs = iterator.next();
            if(ucis.contains(cs.getCid())){
                //
                continue;
            }
            grpcClient.sender(cs.getHost(),cs.getPort(),msgRequest);
        }

    }
}

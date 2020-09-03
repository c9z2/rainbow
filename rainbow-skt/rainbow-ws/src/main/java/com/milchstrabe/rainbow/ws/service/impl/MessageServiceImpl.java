package com.milchstrabe.rainbow.ws.service.impl;

import com.google.gson.Gson;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.base.server.typ3.grpc.Msg;
import com.milchstrabe.rainbow.server.domain.dto.Message;
import com.milchstrabe.rainbow.ws.repository.ClientServerRepository;
import com.milchstrabe.rainbow.ws.server.typ3.grpc.GRPCClient;
import com.milchstrabe.rainbow.ws.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author ch3ng
 * @Date 2020/4/28 16:44
 * @Version 1.0
 * @Description
 **/
@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ClientServerRepository clientServerRepository;

    @Autowired
    private GRPCClient grpcClient;

    @Async("asyncExecutor")
    @Override
    public void doMessage(Message message) {
        String receiver = message.getReceiver();
        Gson gson = new Gson();
        String json = gson.toJson(message);

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

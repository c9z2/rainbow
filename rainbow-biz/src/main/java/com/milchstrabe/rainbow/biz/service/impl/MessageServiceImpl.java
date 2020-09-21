package com.milchstrabe.rainbow.biz.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.api.typ3.grpc.Msg;
import com.milchstrabe.rainbow.biz.domain.dto.SendMessageDTO;
import com.milchstrabe.rainbow.biz.repository.ClientServerRepository;
import com.milchstrabe.rainbow.biz.service.IMessageService;
import com.milchstrabe.rainbow.biz.typ3.grpc.GRPCClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {


    @Autowired
    private ClientServerRepository clientServerRepository;

    @Autowired
    private GRPCClient grpcClient;

    @Async("asyncExecutor")
    @Override
    public void doMessage(SendMessageDTO dto) {
        //TODO save message
        Msg.MsgRequest msgRequest = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            msgRequest = Msg.MsgRequest.newBuilder()
                    .setMsgId(dto.getId())
                    .setMsgType(dto.getMsgType())
                    .setContent(objectMapper.writeValueAsString(dto.getContent()))
                    .setSender(dto.getSender())
                    .setReceiver(dto.getReceiver())
                    .setDate(dto.getDate())
                    .build();
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
            return;
        }
        Set<ClientServer> css = clientServerRepository.findCSByUid(dto.getReceiver());
        Iterator<ClientServer> iterator = css.iterator();
        while (iterator.hasNext()){
            ClientServer cs = iterator.next();
            grpcClient.sender(cs.getHost(),cs.getPort(),msgRequest);
        }
    }
}

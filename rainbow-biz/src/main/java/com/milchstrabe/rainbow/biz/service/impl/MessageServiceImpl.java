package com.milchstrabe.rainbow.biz.service.impl;

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

        Msg.MsgRequest msgRequest = Msg.MsgRequest.newBuilder()
                .setMsgId(dto.getId())
                .setMsgType(dto.getMsgType())
                .setContent(dto.getContent().toJSONString())
                .setSender(dto.getSender())
                .setReceiver(dto.getReceiver())
                .setDate(dto.getDate())
                .build();

        Set<ClientServer> css = clientServerRepository.findCSByUid(dto.getReceiver());
        Iterator<ClientServer> iterator = css.iterator();
        while (iterator.hasNext()){
            ClientServer cs = iterator.next();
            grpcClient.sender(cs.getHost(),cs.getPort(),msgRequest);
        }

    }
}

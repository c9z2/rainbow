package com.milchstrabe.rainbow.skt.service.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.UCI;
import com.milchstrabe.rainbow.skt.repository.ClientServerRepository;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.session.Session;
import com.milchstrabe.rainbow.skt.server.session.SessionManager;
import com.milchstrabe.rainbow.skt.server.typ3.grpc.GRPCClient;
import com.milchstrabe.rainbow.skt.server.typ3.grpc.Msg;
import com.milchstrabe.rainbow.skt.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private ClientServerRepository clientServerRepository;

    @Autowired
    private GRPCClient grpcClient;

    @Override
    public boolean doMessage(Data.Request request) throws LogicException {
        ByteString data = request.getData();
        Msg.MsgRequest msgRequest = null;
        List<String> ucis = new ArrayList<>();
        try {
            msgRequest = Msg.MsgRequest.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage());
            throw new LogicException(5003,e.getMessage());
        }
        String receiver = msgRequest.getReceiver();
        //current server node sessions
        Map<String, Session> sessions = SessionManager.getSession(receiver);
        if(sessions.size() > 0){
            Data.Response resp = Data.Response.newBuilder()
                    .setCmd1(request.getCmd1())
                    .setCmd2(request.getCmd2())
                    .setCode(2)
                    .setData(request.getData())
                    .build();
            Iterator<Map.Entry<String, Session>> iterator = sessions.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Session> sessionEntry = iterator.next();
                Session session = sessionEntry.getValue();
                UCI uci = (UCI)session.getAttachment();
                ucis.add(uci.getCid());
                session.write(resp);
            }
        }
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
        return true;
    }

}

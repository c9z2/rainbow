package com.milchstrabe.rainbow.skt.service.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.milchstrabe.rainbow.skt.repository.ClientServerRepository;
import com.milchstrabe.rainbow.skt.server.ClientServer;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import com.milchstrabe.rainbow.skt.server.grpc.GRPCClient;
import com.milchstrabe.rainbow.skt.server.grpc.Msg;
import com.milchstrabe.rainbow.skt.server.tcp.codc.TCPResponse;
import com.milchstrabe.rainbow.skt.server.tcp.session.Session;
import com.milchstrabe.rainbow.skt.server.tcp.session.SessionManager;
import com.milchstrabe.rainbow.skt.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public boolean doMessage(Data.Request request) {
        ByteString data = request.getData();
        try {
            Msg.MsgRequest msgRequest = Msg.MsgRequest.parseFrom(data);
            String receiver = msgRequest.getReceiver();
            Session session = SessionManager.getSession(receiver);
            if(session != null){
                Data.Response resp = Data.Response.newBuilder()
                        .setCmd1(request.getCmd1())
                        .setCmd2(request.getCmd2())
                        .setCode(2)
                        .setData(request.getData())
                        .build();
                TCPResponse tcpResponse = TCPResponse.builder()
                        .response(resp)
                        .build();
                session.write(tcpResponse);
                return true;
            }
            List<ClientServer> css = clientServerRepository.findCSByUid(receiver);
            if(css == null){
                //TODO offline message
                //receiver offline
                //save offline message 2 db
                return true;
            }
            css.stream().forEach(cs->{
                //TODO gRPC fail
                grpcClient.sender(cs.getIp(),cs.getPort(),msgRequest);
            });

        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage());
        }

        return false;
    }

}

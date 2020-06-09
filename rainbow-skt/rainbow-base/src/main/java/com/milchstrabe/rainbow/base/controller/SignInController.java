package com.milchstrabe.rainbow.base.controller;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.base.common.constant.StateCode;
import com.milchstrabe.rainbow.base.server.annotion.NettyController;
import com.milchstrabe.rainbow.base.server.annotion.NettyMapping;
import com.milchstrabe.rainbow.base.server.codc.Data;
import com.milchstrabe.rainbow.base.server.session.Request;
import com.milchstrabe.rainbow.base.service.ISignInService;
import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.server.domain.vo.SiginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ch3ng
 * @Date 2020/4/21 21:57
 * @Version 1.0
 * @Description
 **/
@Slf4j
@NettyController(cmd = 0)
public class SignInController {

    @Autowired
    private ISignInService signInService;

    /**
     * request->{
     *     "token":"jwt",
     *     "cid":"cid",
     *     "clientType":"MACOS"
     * }
     * @param request
     * @return
     */
    @NettyMapping(cmd = 0)
    public Data.Response signIn(Request request){

        Data.Request dataRequest = request.getRequest();
        ByteString data = dataRequest.getData();
        String json = data.toStringUtf8();
        Gson gson = new Gson();
        SiginVO siginVO = gson.fromJson(json, SiginVO.class);

        try {
            signInService.signIn(siginVO.getToken(), siginVO.getCid(), siginVO.getClientType(), request.getSession());
            return Data.Response
                    .newBuilder()
                    .setCmd1(dataRequest.getCmd1())
                    .setCmd2(dataRequest.getCmd2())
                    .setCode(StateCode.SUCCESS)
                    .build();
        } catch (AuthException e) {
            log.error("[{}]:{}",e.CODE,e.getMessage());
            return Data.Response
                    .newBuilder()
                    .setCmd1(dataRequest.getCmd1())
                    .setCmd2(dataRequest.getCmd2())
                    .setCode(e.CODE)
                    .build();
        }

    }
}

package com.milchstrabe.rainbow.biz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.common.util.SnowFlake;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.SendMessageDTO;
import com.milchstrabe.rainbow.biz.domain.vo.SendingMessageVO;
import com.milchstrabe.rainbow.biz.service.IMessageService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/5/11 11:52
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    private final SnowFlake snowFlake = new SnowFlake(15, 23);

    @Autowired
    private IMessageService messageService;

    @PutMapping(path = APIVersion.V_1 + "/sending")
    public Result<String> sendMessage(@CurrentUser RequestUser user,
                                      @RequestBody @Validated SendingMessageVO vo) throws LogicException {

        SendMessageDTO dto = new SendMessageDTO();
        dto.setReceiver(vo.getReceiver());
        dto.setMsgType(vo.getMsgType());
        dto.setContent(JSONObject.parseObject(JSON.toJSONString(vo.getContent())));
        dto.setDate(System.currentTimeMillis());
        dto.setId(snowFlake.nextId()+"");
        dto.setSender(user.getUserId());

        messageService.doMessage(dto);
        return ResultBuilder.success(dto);
    }
}

package com.milchstrabe.rainbow.biz.domain.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageDTO {

    private String id;
    private Integer msgType;
    private JSONObject content;
    private String sender;
    private String receiver;
    private Short status;
    private Long date;
}

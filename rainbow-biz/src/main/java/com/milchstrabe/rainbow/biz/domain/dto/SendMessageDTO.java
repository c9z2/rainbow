package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class SendMessageDTO {

    private String id;
    private Integer msgType;
    private Map<String,Object> content;
    private String sender;
    private String receiver;
    private Short status;
    private Long date;
}

package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Setter
@Getter
public class SendingMessageVO {

    @NotNull(message = "message type can not be null")
    private Integer msgType;

    @NotNull(message = "message content can not be null")
    private Map<String,Object> content;

    @NotNull(message = "message receiver can not be null")
    private String receiver;


}

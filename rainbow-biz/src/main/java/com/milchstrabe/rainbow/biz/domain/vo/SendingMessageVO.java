package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Setter
@Getter
public class SendingMessageVO {

    @NotNull(message = "message type cant not be null")
    private Integer msgType;

    private Map<String,Object> content;

    @NotNull(message = "receiver cant not be null")
    private String receiver;


}

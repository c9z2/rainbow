package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class HandleAddContactVO {
    @NotNull(message = "sender can not be null")
    private String sender;

    @NotNull(message = "handle can not be null")
    private Short handle;
}

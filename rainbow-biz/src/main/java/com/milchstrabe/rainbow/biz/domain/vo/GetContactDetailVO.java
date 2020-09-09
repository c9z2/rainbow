package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class GetContactDetailVO {
    @NotNull(message = "userId can not be null")
    private String userId;
    @NotNull(message = "contactId can not be null")
    private String contactId;
}

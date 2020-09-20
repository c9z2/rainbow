package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/9/3 15:07
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class AddContactMessageDTO {

    private ContactBriefDTO sender;
    private ContactBriefDTO receiver;

    /**
     * 0: Not processed
     * 1: accept
     * 2: reject
     * 3: timeout
     */
    private Short status;
    private String note;




}

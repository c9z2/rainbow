package com.milchstrabe.rainbow.server.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author ch3ng
 * @Date 2020/9/3 15:07
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class AddContactMessage implements Serializable {

    private ContactBrief sender;
    private ContactBrief receiver;
    /**
     * 0: Not processed
     * 1: accept
     * 2: reject
     * 3: timeout
     */
    private Short status;
    private String note;


}

package com.milchstrabe.rainbow.biz.domain.po;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author ch3ng
 * @Date 2020/5/12 17:20
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class Contact {

    private User user;
    private String remark;
    private Date createTime;
}

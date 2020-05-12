package com.milchstrabe.rainbow.biz.domain.vo;

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
public class ContactVO {

    private String id;
    private String username;
    private String remark;
    private Date createTime;
}

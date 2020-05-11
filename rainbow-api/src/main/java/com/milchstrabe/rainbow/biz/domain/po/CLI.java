package com.milchstrabe.rainbow.biz.domain.po;

import com.milchstrabe.rainbow.biz.domain.po.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author ch3ng
 * @Date 2020/5/11 16:43
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class CLI {

    private Long id;
    private String cid;
    private String ctype;
    private Date createTime;
    private User user;

}

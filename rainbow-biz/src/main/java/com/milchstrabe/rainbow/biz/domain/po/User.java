package com.milchstrabe.rainbow.biz.domain.po;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:56
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class User {
    private String id;
    private String username;
    private String psssword;
}

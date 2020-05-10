package com.milchstrabe.rainbow.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/5/10 15:16
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class UCI {
    private String username;
    private String uid;
    private String cid;
}

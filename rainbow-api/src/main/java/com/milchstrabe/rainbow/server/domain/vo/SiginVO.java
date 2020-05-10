package com.milchstrabe.rainbow.server.domain.vo;

import com.milchstrabe.rainbow.server.domain.ClientType;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/5/10 15:30
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class SiginVO {

    private String token;
    private String cid;
    private ClientType clientType;
}

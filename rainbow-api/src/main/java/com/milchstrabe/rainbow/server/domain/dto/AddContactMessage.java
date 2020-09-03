package com.milchstrabe.rainbow.server.domain.dto;

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
public class AddContactMessage {

    private String avatar;
    private String note;
    private String nickname;
    private String username;

}

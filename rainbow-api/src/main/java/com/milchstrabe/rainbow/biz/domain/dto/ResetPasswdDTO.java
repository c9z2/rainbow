package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/9/3 13:50
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class ResetPasswdDTO {
    private String username;
    private String passwd;
    private String email;
    private String code;
}

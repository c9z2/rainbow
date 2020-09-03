package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author ch3ng
 * @Date 2020/6/5 19:22
 * @Version 1.0
 * @Description
 *
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInVO {

    @NotNull(message = "用户名密码错误")
    private String username;
    @NotNull(message = "用户名密码错误")
    private String passwd;
}

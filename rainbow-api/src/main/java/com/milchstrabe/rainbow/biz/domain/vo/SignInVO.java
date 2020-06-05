package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;

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

    private String username;
    private String passwd;
}

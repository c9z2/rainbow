package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/6/5 19:00
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVO {
    private String username;
    private String passwd;
    private String nickname;
}

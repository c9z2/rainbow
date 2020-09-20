package com.milchstrabe.rainbow.biz.domain.po;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/6/5 15:29
 * @Version 1.0
 * @Description user property
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProperty {
    private String avatar;
    private Short age;
    private String email;
    private Short gender;
    private String signature;
    private String nickname;
    private String phone;
    private Long createTime;
}

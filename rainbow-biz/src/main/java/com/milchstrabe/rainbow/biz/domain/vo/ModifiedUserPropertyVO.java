package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/6/10 19:09
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifiedUserPropertyVO {
    private Short age;
    private String email;
    private Short gender;
    private String remark;
    private String nickname;
    private String phone;
}

package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @Length(max=24,min = 0,message = "0-24个字符")
    private String signature;
    private String nickname;
    private String phone;
}

package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GetContactDetailDTO {

    private String userId;
    private String username;

    private String avatar;
    private Short age;
    private String email;
    private Short gender;

    private String nickname;
    private String phone;
    private String signature;

    private String remark;
    private Long createTime;


}

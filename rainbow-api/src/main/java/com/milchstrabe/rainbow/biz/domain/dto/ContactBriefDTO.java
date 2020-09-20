package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ContactBriefDTO implements Serializable {
    private String avatar;
    private String nickname;
    private String username;
}
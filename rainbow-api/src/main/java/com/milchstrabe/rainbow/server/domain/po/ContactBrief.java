package com.milchstrabe.rainbow.server.domain.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ContactBrief implements Serializable {
    private String avatar;
    private String nickname;
    private String username;
}
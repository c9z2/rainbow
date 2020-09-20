package com.milchstrabe.rainbow.server.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AcceptAddContactMessageDTO {
    private String avatar;
    private Long createTime;
    private String remark;
    private String userId;
    private String username;
}

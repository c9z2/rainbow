package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/6/10 19:09
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class ModifiedContactRemarkDTO {

    private String remark;
    private String userId;
    private String contactId;
}

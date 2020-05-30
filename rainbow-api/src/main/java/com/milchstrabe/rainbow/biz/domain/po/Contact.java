package com.milchstrabe.rainbow.biz.domain.po;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/5/12 17:20
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    private User user;
    private String remark;
    private Long createTime;
}

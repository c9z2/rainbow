package com.milchstrabe.rainbow.biz.domain.po;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:56
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String userId;
    private String username;
    private String passwd;
    private Short status;
    private UserProperty property;

}

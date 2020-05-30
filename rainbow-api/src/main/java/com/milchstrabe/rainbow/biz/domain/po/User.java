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
    private String id;
    private String username;
    private String password;


}

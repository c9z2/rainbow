package com.milchstrabe.rainbow.biz.domain;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/6/10 19:00
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUser {
    private Long id;
    private String userId;
    private String username;
    private String passwd;
    private Short status;
}

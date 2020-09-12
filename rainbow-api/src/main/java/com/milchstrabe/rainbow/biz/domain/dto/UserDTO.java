package com.milchstrabe.rainbow.biz.domain.dto;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:38
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String username;
    private String passwd;
    private Short status;
    private UserPropertyDTO property;

}

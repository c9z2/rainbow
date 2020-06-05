package com.milchstrabe.rainbow.biz.domain.po;

import lombok.*;

import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/6/5 15:35
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Long id;
    private String groupId;
    private String groupName;
    private User creater;
    private List<User> members;
    private Long createTime;
}

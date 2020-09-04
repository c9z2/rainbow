package com.milchstrabe.rainbow.server.domain.dto;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/5/30 21:36
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String id;
    private Integer msgType;
    private String content;
    private String sender;
    private String receiver;
    private Short status;
    private Long date;
}

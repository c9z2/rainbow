package com.milchstrabe.rainbow.ws.domain.dto;

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
public class MessageRequest {

    private String id;
    private Integer msgType;
    private String content;
    private String sender;
    private String receiver;
    private Long date;
}

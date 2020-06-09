package com.milchstrabe.rainbow.ws.domain.vo;

import lombok.*;

/**
 * @Author ch3ng
 * @Date 2020/5/30 21:12
 * @Version 1.0
 * @Description
 *      *      string msgId = 1;
 *      *      int32 msgType = 2;
 *      *      string content = 3;
 *      *      string sender = 4;
 *      *      string receiver = 5;
 *      *      string date = 6;
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String id;
    private Integer msgType;
    private String content;
    private String sender;
    private String receiver;
    private Long date;

}

package com.milchstrabe.rainbow.biz.domain.dto;

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
public class MessageDTO<T> {

    private String id;
    private Integer msgType;
    private T content;
    private String sender;
    private String receiver;
    private Short status;
    private Long date;
}

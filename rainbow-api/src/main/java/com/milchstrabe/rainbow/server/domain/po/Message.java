package com.milchstrabe.rainbow.server.domain.po;

import lombok.*;

import java.io.Serializable;

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
public class Message<T> implements Serializable {

    private String id;
    /**
     * 1: text message
     * 10: add contact message
     */
    private Integer msgType;
    private T content;
    private String sender;
    private String receiver;
    /**
     * -1：发送失败
     * 0:未送达，
     * 1：未读，
     * 2：已读
     */
    private Short status;
    private Long date;
}

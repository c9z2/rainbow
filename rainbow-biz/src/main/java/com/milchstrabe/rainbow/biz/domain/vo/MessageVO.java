package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;

import javax.validation.constraints.NotNull;

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
public class MessageVO {


    @NotNull(message = "message type cant not be null")
    private Integer msgType;

    private AddContactMessageVO content;

    @NotNull(message = "receiver cant not be null")
    private String receiver;
    @NotNull(message = "status cant not be null")
    private Short status;
    private Long date;


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class AddContactMessageVO{
        private String avatar;
        private String note;
        private String nickname;
        private String username;
        private String receiverNickname;
    }
}

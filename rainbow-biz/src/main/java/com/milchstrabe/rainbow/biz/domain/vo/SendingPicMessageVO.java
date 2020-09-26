package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SendingPicMessageVO {

    @NotNull(message = "message receiver can not be null")
    private String receiver;

    private MultipartFile file;



}

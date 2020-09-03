package com.milchstrabe.rainbow.biz.domain.vo;

import com.milchstrabe.rainbow.biz.common.validator.annotation.Regexp;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author ch3ng
 * @Date 2020/9/3 10:54
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class SendResetPwdCheckCodeVO {

    @Regexp(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "电子邮箱地址非法")
    private String email;

    @NotNull(message = "用户名不能为空")
    private String username;
}

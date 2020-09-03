package com.milchstrabe.rainbow.biz.domain.vo;

import com.milchstrabe.rainbow.biz.common.validator.annotation.Regexp;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author ch3ng
 * @Date 2020/9/3 13:19
 * @Version 1.0
 * @Description
 **/

@Setter
@Getter
public class ResetPasswdVO {

    @NotNull(message = "用户名不能为空")
    private String username;

    @Regexp(regexp = "^[a-zA-Z]\\w{6,18}$",message = "以字母开头，长度在6~18之间，只能包含字母、数字和下划线")
    private String passwd;
    @NotNull(message = "电子邮件不能为空")
    private String email;
    @NotNull(message = "验证码不能为空")
    private String code;
}

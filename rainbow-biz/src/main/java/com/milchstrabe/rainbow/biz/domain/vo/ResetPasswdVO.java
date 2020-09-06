package com.milchstrabe.rainbow.biz.domain.vo;

import com.milchstrabe.rainbow.biz.common.validator.annotation.Regexp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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

    @Regexp(regexp = "^[a-z][a-zA-Z0-9_]{4,16}$",message = "用户名以小写字母开头，允许5-16字符，允许字母数字下划线")
    private String username;

    @Regexp(regexp = "^[a-zA-Z]\\w{6,18}$",message = "密码以字母开头，长度在6~18之间，只能包含字母、数字和下划线")
    private String passwd;

    @Regexp(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "电子邮箱地址非法")
    private String email;

    @Regexp(regexp = "^\\w{6}$",message = "验证码错误")
    private String code;
}

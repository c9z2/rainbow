package com.milchstrabe.rainbow.biz.domain.vo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author ch3ng
 * @Date 2020/6/10 19:09
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class ModifiedContactRemarkVO {

    @NotNull(message = "备注不能为空")
    @Length(min = 1,max = 12,message = "")
    private String remark;
    @NotNull(message = "联系人不能空")
    private String contactId;
}

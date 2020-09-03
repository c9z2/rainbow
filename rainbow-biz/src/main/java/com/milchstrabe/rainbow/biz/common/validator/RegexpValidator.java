package com.milchstrabe.rainbow.biz.common.validator;

import com.milchstrabe.rainbow.biz.common.validator.annotation.Regexp;
import com.sun.org.apache.bcel.internal.generic.ATHROW;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ch3ng
 * @Date 2020/9/3 09:22
 * @Version 1.0
 * @Description
 **/
public class RegexpValidator implements ConstraintValidator<Regexp,String>{

    private Pattern parttern;

    @Override
    public void initialize(Regexp regexp) {
        this.parttern = Pattern.compile(regexp.regexp());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 不为null才进行校验
        if (value != null) {
            Matcher matcher = parttern.matcher(value);
            return matcher.find();
        }
        return false;
    }
}

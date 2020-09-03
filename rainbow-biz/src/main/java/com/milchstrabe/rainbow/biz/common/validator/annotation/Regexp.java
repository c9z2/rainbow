package com.milchstrabe.rainbow.biz.common.validator.annotation;

import com.milchstrabe.rainbow.biz.common.validator.RegexpValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RegexpValidator.class})
public @interface Regexp {

    String message() default "";

    String regexp() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
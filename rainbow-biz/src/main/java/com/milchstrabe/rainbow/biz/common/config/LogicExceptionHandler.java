package com.milchstrabe.rainbow.biz.common.config;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.exception.AuthException;
import com.milchstrabe.rainbow.exception.InternalException;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @Author ch3ng
 * @Date 2020/3/30 13:53
 * @Version 1.0
 * @Description global exception handler
 * RNTLWXJTNZKJPKBP
 * IAVEGDPLRCJFEVON
 **/
@Slf4j
@RestControllerAdvice
public class LogicExceptionHandler {

    @ExceptionHandler(value = AuthException.class)
    public Result internalException(AuthException exception){
        return ResultBuilder.noAuth(exception.getMessage());
    }

    @ExceptionHandler(value = InternalException.class)
    public Result internalException(InternalException exception){
        return ResultBuilder.fail(exception.getMessage());
    }

    @ExceptionHandler(value = ParamMissException.class)
    public Result paramMissException(ParamMissException exception){
        return ResultBuilder.fail(exception.getMessage());
    }


    @ExceptionHandler(value = LogicException.class)
    public Result logicException(LogicException exception){
        return ResultBuilder.fail(exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String msg = null;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            msg = fieldError.getDefaultMessage();
        }
        return ResultBuilder.fail(msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        return ResultBuilder.fail(ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public Result exception(Exception ex) {
        log.error("eror:",ex);
        return ResultBuilder.fail(ex.getMessage());
    }
}

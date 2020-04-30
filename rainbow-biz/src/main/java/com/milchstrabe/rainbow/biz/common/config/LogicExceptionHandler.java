package com.milchstrabe.rainbow.biz.common.config;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.exception.LogicException;
import com.milchstrabe.rainbow.biz.exception.ParamMissException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author ch3ng
 * @Date 2020/3/30 13:53
 * @Version 1.0
 * @Description global exception handler
 **/
@RestControllerAdvice
public class LogicExceptionHandler {


    @ExceptionHandler(value = ParamMissException.class)
    public Result paramMissException(ParamMissException exception){
        return ResultBuilder.fail(exception.getMessage());
    }


    @ExceptionHandler(value = LogicException.class)
    public Result logicException(LogicException exception){
        return ResultBuilder.fail(exception.getMessage());
    }

}

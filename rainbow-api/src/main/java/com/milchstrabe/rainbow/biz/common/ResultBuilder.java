package com.milchstrabe.rainbow.biz.common;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:45
 * @Version 1.0
 * @Description
 **/
public class ResultBuilder<T> {

    public static Result success(){
        Result result = new Result<>(ResultCode.SUCCESS);
        return result;
    }

    public static <T> Result success(T t){
        Result result = new Result<>(ResultCode.SUCCESS,t);
        return result;
    }

    public static Result fail(){
        Result result = new Result<>(ResultCode.FAIL);
        return result;
    }

    public static <T> Result fail(String msg){
        Result result = new Result<>(ResultCode.FAIL.code(), msg);
        return result;
    }

    public static <T> Result exception(T t){
        Result result = new Result<>(ResultCode.EXCEPTION, t);
        return result;
    }
}

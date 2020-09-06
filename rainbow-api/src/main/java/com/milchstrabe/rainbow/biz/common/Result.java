package com.milchstrabe.rainbow.biz.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:58
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    protected Result(ResultCode resultCode,T t){
        this.code = resultCode.code();
        this.msg = resultCode.msg();
        this.data = t;
    }

    protected Result(ResultCode resultCode){
        this.code = resultCode.code();
        this.msg = resultCode.msg();
    }

    protected Result(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }



}

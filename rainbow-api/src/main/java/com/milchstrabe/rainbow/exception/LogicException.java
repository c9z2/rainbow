package com.milchstrabe.rainbow.exception;

/**
 * @Author ch3ng
 * @Date 2020/4/29 22:43
 * @Version 1.0
 * @Description
 **/
public class LogicException extends Exception {
    public int CODE = 5000;
    public LogicException(int code,String msg){
        super(msg);
        this.CODE = code;
    }
}

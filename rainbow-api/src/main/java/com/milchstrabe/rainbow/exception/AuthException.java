package com.milchstrabe.rainbow.exception;

/**
 * @Author ch3ng
 * @Date 2020/4/30 14:24
 * @Version 1.0
 * @Description
 **/
public class AuthException extends Exception{

    public final int CODE = 5001;

    public AuthException(String msg){
        super(msg);
    }
}

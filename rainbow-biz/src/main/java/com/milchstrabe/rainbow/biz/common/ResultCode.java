package com.milchstrabe.rainbow.biz.common;

public enum ResultCode {

    SUCCESS(200,"success"),
    FAIL(300,"fail"),
    EXCEPTION(500,"exception");

    private Integer code;
    private String msg;

    private ResultCode(Integer code,String msg){
        this.code = code;
        this.msg =msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}

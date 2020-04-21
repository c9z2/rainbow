package com.milchstrabe.rainbow.skt.common.constant;

/**
 * @Author ch3ng
 * @Date 2020/4/21 19:14
 * @Version 1.0
 * @Description
 **/
public class StateCode {

    /**
     * 服务器内部错误
     */
    public  static final byte ERROR= 5;

    /**
     * 正常
     */
    public  static final byte NORMAL= 2;

    /**
     * 缺少参数
     */
    public  static final byte MISS= 3;

    /**
     * 指令不存在
     */
    public  static final byte NOT_FOUND= 4;

    /**
     * 下发指令的状态码
     */
    public static final byte DOWN_STATE = 6;

    /**
     * 没有权限没有登入
     */
    public static final byte NO_AUTH = 7;



}

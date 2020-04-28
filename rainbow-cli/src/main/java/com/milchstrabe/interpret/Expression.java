package com.milchstrabe.interpret;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:37
 * @Version 1.0
 * @Description
 **/
public interface Expression {

   boolean interpret(String[] cmds);
}
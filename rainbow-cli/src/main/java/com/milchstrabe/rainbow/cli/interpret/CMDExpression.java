package com.milchstrabe.rainbow.cli.interpret;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:37
 * @Version 1.0
 * @Description
 **/
public interface CMDExpression {

   boolean interpret(String line);
}
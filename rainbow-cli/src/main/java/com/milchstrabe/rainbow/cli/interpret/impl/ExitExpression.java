package com.milchstrabe.rainbow.cli.interpret.impl;

import com.milchstrabe.rainbow.cli.interpret.Expression;

/**
 * @Author ch3ng
 * @Date 2020/4/27 00:04
 * @Version 1.0
 * @Description
 **/
public class ExitExpression implements Expression {

    @Override
    public boolean interpret(String[] cmds) {
        System.exit(1);
        return true;
    }
}

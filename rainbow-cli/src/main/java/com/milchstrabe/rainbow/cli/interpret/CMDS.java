package com.milchstrabe.rainbow.cli.interpret;

import com.milchstrabe.rainbow.cli.interpret.impl.ExitExpression;
import com.milchstrabe.rainbow.cli.interpret.impl.SendExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:44
 * @Version 1.0
 * @Description
 **/
public class CMDS {

    public final static Map<String,Expression> C_M_D_S = new HashMap();

    static{
        C_M_D_S.put("send",new SendExpression());
        C_M_D_S.put("exit",new ExitExpression());
    }
}

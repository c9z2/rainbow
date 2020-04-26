package com.milchstrabe.interpret;

import com.milchstrabe.interpret.impl.SendExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ch3ng
 * @Date 2020/4/26 23:44
 * @Version 1.0
 * @Description
 **/
public class CMDS {

    private final static Map<String,Expression> C_M_D_S = new HashMap();

    static{
        C_M_D_S.put("send",new SendExpression());
    }
}

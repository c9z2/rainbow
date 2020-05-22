package com.milchstrabe.rainbow.ws.common;

import com.milchstrabe.rainbow.server.domain.UCI;

import java.security.Principal;

public class DefaultPrincipal implements Principal {

    private UCI uci;

    public DefaultPrincipal(UCI uci){
        this.uci = uci;
    }

    @Override
    public String getName() {
        return uci.getUid();
    }

}
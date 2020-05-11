package com.milchstrabe;

import com.milchstrabe.rainbow.cli.controller.ControllerContext;
import com.milchstrabe.rainbow.cli.controller.Invoker;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.channel.Channel;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;

/**
 * Unit test for simple App.
 */
public class ControllerTest
{

    @Test
    public void testControllerBean(){
        ControllerContext controllerContext = new ControllerContext();
        Invoker invoker = controllerContext.getInvoker(0, 0);
        Channel channel = null;
        invoker.invoke(Data.Response.newBuilder().build(),channel);
    }
}

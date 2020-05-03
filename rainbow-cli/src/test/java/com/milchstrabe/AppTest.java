package com.milchstrabe;

import static org.junit.Assert.assertTrue;

import com.google.protobuf.ByteString;
import com.milchstrabe.rainbow.cli.client.UDPClient;
import com.milchstrabe.rainbow.skt.server.codc.Data;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
//        Data.Request request = Data.Request.newBuilder()
//                .setCmd1(0)
//                .setCmd2(1)
//                .setData(ByteString.copyFromUtf8("123"))
//                .build();

        UDPClient udpClient = new UDPClient();
        udpClient.start();

//        udpClient.channel.writeAndFlush(request);
    }
}

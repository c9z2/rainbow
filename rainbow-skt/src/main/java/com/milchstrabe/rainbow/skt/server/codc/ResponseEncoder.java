package com.milchstrabe.rainbow.skt.server.codc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * +--------+----------+-----------+-------+---------+
 * |  包头  |  一级指令 |  二级指令 |  长度 |  数据   |
 * +--------+----------+----------+-------+----------+
 * |  4byte |  1byte   |   1byte  | 4byte |more byte|
 * +--------+----------+----------+-------+----------+
 * +--------+-----------+----------+----------+--------+-----------+
 * |  包头  |  一级指令 |  二级指令 |  状态码  |  长度  |     数据   |
 * +--------+----------+----------+----------+--------+-----------+
 * |  4byte |  1byte   |   1byte  |   1byte  | 4byte  |  more byte|
 * +--------+----------+----------+---------+--------+-----------+
 *
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response response, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(response.getHead())
                .writeByte(response.getFirstOrder())
                .writeShort(response.getSecondOrder())
                .writeByte(response.getStateCode())
                .writeInt(response.getData() == null ? 0:response.getData().length)
                .writeBytes(response.getData() == null ? new byte[0] : response.getData());
    }
}

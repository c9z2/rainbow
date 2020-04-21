package com.milchstrabe.rainbow.skt.server.codc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/4/20 20:39
 * @Version 1.0
 * @Description
 **/
@Slf4j
public class RequestDecoder extends ByteToMessageDecoder {

    private static final int BASE_LENTH = 10;
    private static final int HEAD = -1431655766;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (true){
            if(byteBuf.readableBytes() >= BASE_LENTH){
//                int beginIndex;
                while (true){
                    byteBuf.markReaderIndex();
//                    beginIndex = byteBuf.readerIndex();
                    int tmpHead = 0;
                    if((tmpHead = byteBuf.readInt()) == HEAD){
                        break;
                    }
                    log.info("package header："+tmpHead);
                    byteBuf.resetReaderIndex();
                    byteBuf.readByte();
                    if(byteBuf.readableBytes() < BASE_LENTH){
                        return;
                    }
                }
                byte cmdOf1st = byteBuf.readByte();
                byte cmdOf2nd = byteBuf.readByte();
                int dataLength = byteBuf.readInt();
                if(dataLength < 0){
                    channelHandlerContext.channel().close();
                }
                if(byteBuf.readableBytes() < dataLength){
                    byteBuf.resetReaderIndex();
//                    byteBuf.readerIndex(beginIndex);
                    return;
                }
                byte[] data = new byte[dataLength];
                byteBuf.readBytes(data);

                list.add(
                        Request.builder()
                        .data(data)
                        .firstOrder(cmdOf1st)
                        .secondOrder(cmdOf2nd)
                        .build()
                );
            }else{
                break;
            }
        }
        return;
    }

}

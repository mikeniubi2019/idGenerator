package com.mike.idgenerator.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class ReceiveIdsDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.isReadable(4)){
            int length = byteBuf.readInt()/8;
            long[] longs = new long[length];
            for (int index = 0 ;index < length;index++){
                longs[index] = byteBuf.readLong();
            }
            list.add(longs);
        }
    }
}

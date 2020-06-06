package com.mike.idgenerator.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IdsSendEncode extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (o instanceof long[]){
            long[] ids = (long[]) o;
            byteBuf.writeInt(ids.length*8);
            for (long l : ids){
                byteBuf.writeLong(l);
            }
            channelHandlerContext.writeAndFlush(byteBuf.retain());
        }
    }
}

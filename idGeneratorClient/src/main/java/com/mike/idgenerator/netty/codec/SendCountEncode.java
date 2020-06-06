package com.mike.idgenerator.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SendCountEncode extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (o instanceof Integer){
            int count = (Integer) o;
            byteBuf.writeInt(count);
            channelHandlerContext.writeAndFlush(byteBuf.retain());
        }
    }
}

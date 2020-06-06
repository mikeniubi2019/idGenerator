package com.mike.idgenerator.netty.channleHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;



@ChannelHandler.Sharable
public class ReceiveChannelHandler extends ChannelInboundHandlerAdapter {
    private ArrayBlockingQueue arrayBlockingQueue;
    public ReceiveChannelHandler(ArrayBlockingQueue arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof long[]){
            long[] temp = (long[])msg;
            arrayBlockingQueue.put(temp);
            ReferenceCountUtil.release(msg);
        }
        else ctx.fireChannelRead(msg);
    }
}

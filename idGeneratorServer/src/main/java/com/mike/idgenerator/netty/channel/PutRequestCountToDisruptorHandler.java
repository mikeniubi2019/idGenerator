package com.mike.idgenerator.netty.channel;

import com.lmax.disruptor.RingBuffer;
import com.mike.idgenerator.pojo.ChannelContexAndRequestCountHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
public class PutRequestCountToDisruptorHandler extends ChannelInboundHandlerAdapter {
    private RingBuffer ringBuffer;

    public PutRequestCountToDisruptorHandler(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Integer){
            Integer count = (Integer) msg;
            publishChannelContexAndRequestCountHolder(ctx,count);
            ReferenceCountUtil.release(msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private void publishChannelContexAndRequestCountHolder(ChannelHandlerContext ctx, Integer count){
        long seq = this.ringBuffer.next();
        ChannelContexAndRequestCountHolder channelContexAndRequestCountHolder = (ChannelContexAndRequestCountHolder)ringBuffer.get(seq);
        channelContexAndRequestCountHolder.setCount(count);
        channelContexAndRequestCountHolder.setChannelHandlerContext(ctx);
        this.ringBuffer.publish(seq);
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        for (StackTraceElement stackTraceElement : cause.getStackTrace()){
//            System.out.println(stackTraceElement);
//        }
//    }
}

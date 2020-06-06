package com.mike.idgenerator.disruptor.handler;

import com.lmax.disruptor.WorkHandler;
import com.mike.idgenerator.pojo.ChannelContexAndRequestCountHolder;
import com.mike.idgenerator.utils.IdGeneratorAndQueueHolder;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GetAndSendResultHandler implements WorkHandler<ChannelContexAndRequestCountHolder> {
    private IdGeneratorAndQueueHolder generatorAndQueueHolder;

    public GetAndSendResultHandler(IdGeneratorAndQueueHolder generatorAndQueueHolder) {
        this.generatorAndQueueHolder = generatorAndQueueHolder;
    }
    private static AtomicLong atomicLong = new AtomicLong(0);
    @Override
    public void onEvent(ChannelContexAndRequestCountHolder channelContexAndRequestCountHolder){
        int count = channelContexAndRequestCountHolder.getCount();
        ChannelHandlerContext channelHandlerContext = channelContexAndRequestCountHolder.getChannelHandlerContext();
        ArrayBlockingQueue<Long> arrayBlockingQueue = this.generatorAndQueueHolder.getArrayBlockingQueue();
        long[] ids = new long[count];
        for (int i=0;i<count;i++){
            try {
                ids[i] = arrayBlockingQueue.poll(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        channelHandlerContext.writeAndFlush(ids);
    }


}

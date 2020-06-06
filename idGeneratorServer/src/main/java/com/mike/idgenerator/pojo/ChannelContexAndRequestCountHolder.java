package com.mike.idgenerator.pojo;

import io.netty.channel.ChannelHandlerContext;

public class ChannelContexAndRequestCountHolder {
    private ChannelHandlerContext channelHandlerContext;
    private int count;

    public ChannelContexAndRequestCountHolder(ChannelHandlerContext channelHandlerContext, int count) {
        this.channelHandlerContext = channelHandlerContext;
        this.count = count;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ChannelContexAndRequestCountHolder() {
    }
}

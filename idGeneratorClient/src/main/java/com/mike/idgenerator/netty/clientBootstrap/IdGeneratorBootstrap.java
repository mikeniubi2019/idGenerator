package com.mike.idgenerator.netty.clientBootstrap;

import com.mike.idgenerator.netty.channleHandler.ReceiveChannelHandler;
import com.mike.idgenerator.netty.codec.ReceiveIdsDecode;
import com.mike.idgenerator.netty.codec.SendCountEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class IdGeneratorBootstrap {
    private Bootstrap bootstrap;
    private int remotePort;
    private String remoteAddress;
    private Channel channel;
    private EventLoopGroup worker;
    private ArrayBlockingQueue arrayBlockingQueue;

    public void initBootstrap(){
        this.bootstrap = new Bootstrap();
        this.worker = new NioEventLoopGroup();

        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(1024*1024,0,4));
                        channelPipeline.addLast(new ReceiveIdsDecode());
                        channelPipeline.addLast(new ReceiveChannelHandler(arrayBlockingQueue));
                        channelPipeline.addLast(new SendCountEncode());
                    }
                });
    }


    public Channel start(){
        try {
            ChannelFuture channelFuture = this.bootstrap.connect(this.remoteAddress,this.remotePort).sync();
            this.channel = channelFuture.channel();
            return this.channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void shutdown(){
        this.channel.close();
        this.worker.shutdownGracefully();
    }

    public IdGeneratorBootstrap(int remotePort, String remoteAddress,ArrayBlockingQueue arrayBlockingQueue) {
        this.remotePort = remotePort;
        this.remoteAddress = remoteAddress;
        this.arrayBlockingQueue = arrayBlockingQueue;
    }
}

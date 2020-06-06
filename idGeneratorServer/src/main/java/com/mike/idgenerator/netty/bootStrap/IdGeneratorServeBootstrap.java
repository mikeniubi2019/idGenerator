package com.mike.idgenerator.netty.bootStrap;

import com.lmax.disruptor.RingBuffer;
import com.mike.idgenerator.netty.channel.PutRequestCountToDisruptorHandler;
import com.mike.idgenerator.netty.codec.IdsCountDecode;
import com.mike.idgenerator.netty.codec.IdsSendEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class IdGeneratorServeBootstrap {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private int port;
    private String add;
    private ServerBootstrap serverBootstrap;
    private RingBuffer ringBuffer;
    private ChannelFuture channelFuture;

    public void start(){
        this.serverBootstrap = new ServerBootstrap();
        this.boss = new NioEventLoopGroup();
        this.worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        serverBootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_BACKLOG,1024*1024)
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        channelPipeline.addLast(new FixedLengthFrameDecoder(4));
                        channelPipeline.addLast(new IdsCountDecode());
                        channelPipeline.addLast(new IdsSendEncode());
                        channelPipeline.addLast(new PutRequestCountToDisruptorHandler(ringBuffer));

                    }
                });

        try {
            this.channelFuture = serverBootstrap.bind(add,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.channelFuture.channel().close();
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }
    public IdGeneratorServeBootstrap(int port, String add) {
        this.port = port;
        this.add = add;
    }

    public RingBuffer getRingBuffer() {
        return ringBuffer;
    }

    public void setRingBuffer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
}

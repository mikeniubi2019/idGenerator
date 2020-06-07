package com.mike.idgenerator.client;

import com.mike.idgenerator.netty.clientBootstrap.IdGeneratorBootstrap;
import io.netty.channel.Channel;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;


public class IdGeneratorClient {
    private Map properties = new HashMap(16);
    public static String ADDRESS = "address";
    public static String PORT = "port";
    private ArrayBlockingQueue<long[]> longsBlokingQueue = new ArrayBlockingQueue(1000);

    private String address;
    private int port;
    private Channel channel;
    private IdGeneratorBootstrap idGeneratorBootstrap;

    private void ininitProperties() {
        this.address = properties.containsKey(ADDRESS)? (String) this.properties.get(ADDRESS) :"localhost";
        this.port = properties.containsKey(PORT)? (int) this.properties.get(PORT) :8080;
    }

    public IdGeneratorClient option(String key,Object value){
        this.properties.put(key,value);
        return this;
    }

    public void build() throws Exception {
        ininitProperties();
        this.idGeneratorBootstrap = new IdGeneratorBootstrap(this.port,this.address,this.longsBlokingQueue);
        this.idGeneratorBootstrap.initBootstrap();
        this.channel = idGeneratorBootstrap.start();
        if (this.channel==null||!this.channel.isOpen()) throw new Exception("创建客户端失败");
    }

    public long[] getIds(int count) throws TimeoutException {
        this.channel.writeAndFlush(count);
        long[] results = new long[0];
        try {
            results = longsBlokingQueue.poll(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (results==null||results.length<1) throw new TimeoutException();
        return results;
    }
}

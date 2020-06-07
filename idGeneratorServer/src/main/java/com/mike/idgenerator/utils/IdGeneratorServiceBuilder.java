package com.mike.idgenerator.utils;

import com.mike.idgenerator.disruptor.DisruptorFactory;
import com.mike.idgenerator.netty.bootStrap.IdGeneratorServeBootstrap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class IdGeneratorServiceBuilder {
    private Map properties = new HashMap(16);
    private DisruptorFactory disruptorFactory;
    private IdGeneratorServeBootstrap idGeneratorServeBootstrap;
    private IdGeneratorAndQueueHolder[] idGeneratorAndQueueHolders;

    public static String ADDRESS = "address";
    public static String PORT = "port";
    public static String IS_FAST = "isFast";
    public static String PERIOD_TIME = "periodTime";
    public static String RANDOMD_BOUND = "randomBound";

    private String address;
    private int port;
    private boolean isFast;
    private int periodTime;
    private int randomBound;

    public  void build(){
        ininitProperties();
        gerneratorIdGeneratorAndQueueHolderContainer();
        createIdGeneratorAndQueueHolders();
        this.disruptorFactory = new DisruptorFactory(this.idGeneratorAndQueueHolders);
        this.disruptorFactory.buildInformation();
        this.idGeneratorServeBootstrap = new IdGeneratorServeBootstrap(this.port,this.address);
        this.idGeneratorServeBootstrap.setRingBuffer(this.disruptorFactory.getRingBuffer());
        this.disruptorFactory.start();
        this.idGeneratorServeBootstrap.start();
        GereratorIdToQueueWorker gereratorIdToQueueWorker = new GereratorIdToQueueWorker(idGeneratorAndQueueHolders);
        gereratorIdToQueueWorker.start();
    }

    private void gerneratorIdGeneratorAndQueueHolderContainer(){
        this.idGeneratorAndQueueHolders = new IdGeneratorAndQueueHolder[Runtime.getRuntime().availableProcessors()];
    }

    private void ininitProperties() {
        this.address = properties.containsKey(ADDRESS)? (String) this.properties.get(ADDRESS) :"localhost";
        this.port = properties.containsKey(PORT)? (int) this.properties.get(PORT) :8080;
        this.isFast = properties.containsKey(IS_FAST)? (boolean) this.properties.get(IS_FAST) :false;
        this.periodTime = properties.containsKey(PERIOD_TIME)? (int) this.properties.get(PERIOD_TIME) :30;
        this.randomBound = properties.containsKey(RANDOMD_BOUND)? (int) this.properties.get(RANDOMD_BOUND) :30;
    }

    public IdGeneratorServiceBuilder option(String key,Object value){
        this.properties.put(key,value);
        return this;
    }

    public void createIdGeneratorAndQueueHolders(){
        for (int index=0;index<Runtime.getRuntime().availableProcessors();index++){
            //TODO 在这里加入id生成器配置信息
            IdGeneratorUtils idGeneratorUtils = new IdGeneratorUtils(this.isFast,this.randomBound,this.periodTime);
            idGeneratorUtils.setDefaultMachinId(index);
            ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(1000);
            IdGeneratorAndQueueHolder idGeneratorAndQueueHolder = new IdGeneratorAndQueueHolder(idGeneratorUtils,arrayBlockingQueue);
            idGeneratorAndQueueHolders[index] = idGeneratorAndQueueHolder;
        }
    }
}

package com.mike.idgenerator.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class IdGeneratorAndQueueHolder {
    private IdGeneratorUtils idGeneratorUtils;
    private ArrayBlockingQueue arrayBlockingQueue;

    public IdGeneratorAndQueueHolder(IdGeneratorUtils idGeneratorUtils, ArrayBlockingQueue arrayBlockingQueue) {
        this.idGeneratorUtils = idGeneratorUtils;
        this.arrayBlockingQueue = arrayBlockingQueue;
    }

    public IdGeneratorUtils getIdGeneratorUtils() {
        return idGeneratorUtils;
    }

    public void setIdGeneratorUtils(IdGeneratorUtils idGeneratorUtils) {
        this.idGeneratorUtils = idGeneratorUtils;
    }

    public ArrayBlockingQueue getArrayBlockingQueue() {
        return arrayBlockingQueue;
    }

    public void setArrayBlockingQueue(ArrayBlockingQueue arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }
}

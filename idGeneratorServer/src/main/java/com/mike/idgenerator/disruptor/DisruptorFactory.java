package com.mike.idgenerator.disruptor;

import com.lmax.disruptor.*;
import com.mike.idgenerator.disruptor.handler.GetAndSendResultHandler;
import com.mike.idgenerator.pojo.ChannelContexAndRequestCountHolder;
import com.mike.idgenerator.utils.IdGeneratorAndQueueHolder;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorFactory {
    private RingBuffer ringBuffer;
    private int ringBufferSize = 1024*1024;
    private SequenceBarrier sequenceBarrier;
    private WorkerPool workerPool;
    private ExecutorService executorService;
    private IdGeneratorAndQueueHolder[] generatorAndQueueHolders;

    public void buildInformation(){
        WorkHandler[] workHandlers = new WorkHandler[generatorAndQueueHolders.length];
        this.ringBuffer = RingBuffer.createMultiProducer(()-> new ChannelContexAndRequestCountHolder(),this.ringBufferSize);
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        for (int index=0;index<generatorAndQueueHolders.length;index++){
            workHandlers[index] = new GetAndSendResultHandler(generatorAndQueueHolders[index]);
        }
        this.workerPool = new WorkerPool(this.ringBuffer, this.sequenceBarrier, new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable throwable, long l, Object o) {
                Arrays.stream(throwable.getStackTrace()).forEach(System.out::print);
            }

            @Override
            public void handleOnStartException(Throwable throwable) {
                Arrays.stream(throwable.getStackTrace()).forEach(System.out::print);
            }

            @Override
            public void handleOnShutdownException(Throwable throwable) {
                Arrays.stream(throwable.getStackTrace()).forEach(System.out::print);
            }
        },workHandlers);
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void start(){
        this.workerPool.start(executorService);
    }

    public void shutDown(){
        if (this.workerPool.isRunning()){
            if (!this.executorService.isShutdown()){
                this.executorService.shutdown();
            }
        }
    }

    public DisruptorFactory(IdGeneratorAndQueueHolder[] generatorAndQueueHolders) {
        this.generatorAndQueueHolders = generatorAndQueueHolders;
    }

    public RingBuffer getRingBuffer() {
        return ringBuffer;
    }

    public void setRingBuffer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
}

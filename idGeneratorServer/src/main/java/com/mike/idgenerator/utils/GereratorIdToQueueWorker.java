package com.mike.idgenerator.utils;


public class GereratorIdToQueueWorker {
    private IdGeneratorAndQueueHolder[] idGeneratorAndQueueHolders;


    public GereratorIdToQueueWorker(IdGeneratorAndQueueHolder[] idGeneratorAndQueueHolders) {
        this.idGeneratorAndQueueHolders = idGeneratorAndQueueHolders;
    }

    public void start(){
        for (int index=0;index<this.idGeneratorAndQueueHolders.length;index++){
            startWork(index,idGeneratorAndQueueHolders[index]);
        }
    }
    public void startWork(int index, IdGeneratorAndQueueHolder idGeneratorAndQueueHolder){
        Thread thread = new Thread(new workerThread(index,idGeneratorAndQueueHolder));
        thread.start();
    }
    public void breakRun(int index){
        IdGeneratorAndQueueHolder idGeneratorAndQueueHolder = idGeneratorAndQueueHolders[index];
        Thread thread = new Thread(new workerThread(index,idGeneratorAndQueueHolder));
        thread.start();
    }

    class workerThread implements Runnable{
        private int index;
        private IdGeneratorAndQueueHolder idGeneratorAndQueueHolder;

        public workerThread(int index, IdGeneratorAndQueueHolder idGeneratorAndQueueHolder) {
            this.index = index;
            this.idGeneratorAndQueueHolder = idGeneratorAndQueueHolder;
        }

        @Override
        public void run() {
                try {
                    while (true) {
                        long id = this.idGeneratorAndQueueHolder.getIdGeneratorUtils().generator();
                        this.idGeneratorAndQueueHolder.getArrayBlockingQueue().put(id);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    breakRun(index);
                }
            }

    }
}

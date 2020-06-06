package com.mike.idgenerator.utils;

import java.util.Random;

public class IdGeneratorUtils {

    private Random random = new Random();
    private static final long MACHINE_BITBUCKT = 1048576;    //100000000000000000000
    private static final long COUNT_BUCKET = 1048575>>2;     //001111111111111111111
    private long lastTime;
    private long currentTime;
    private static final byte MACHINE_Bit=20;
    private static final byte TIME_BIT=22;
    private int count;
    private int defaultMachinId=0;
    private boolean isFast=true;

    public static long parseTimeStamp(long id){
        return id>>TIME_BIT;
    }

    public static long parseCount(long id){
        return id&COUNT_BUCKET;
    }

    public static long parseMachine(long id){
        return id&MACHINE_BITBUCKT;
    }

    public long generator(int machineId){
//        if (!checkMachineId(machineId)){
//            machineId = 1 ;
//        }
        long machineCode = generatorMachineCode(machineId);
        long timeCode = generatorTimeStampCode();
        long currentCount = generatorCurrentCount();

        return machineCode+timeCode+currentCount;
    }

    public long generator(){
//        if (!checkMachineId(machineId)){
//            machineId = 1 ;
//        }
        long machineCode = generatorMachineCode(this.defaultMachinId);
        long timeCode = generatorTimeStampCode();
        long currentCount = generatorCurrentCount();
        return machineCode+timeCode+currentCount;
    }
        //防止数据倾斜
    private long generatorCurrentCount() {
        if (isFast){
            if (currentTime==lastTime){
                count+=1;
            }else {
                lastTime = currentTime;
                count=0;
            }
        }else {
            if (currentTime-lastTime<30000){
                count+=1;
            }else {
                lastTime = currentTime;
                count=random.nextInt(50);
            }
        }
        return count;
    }

    private long generatorTimeStampCode() {
        currentTime = System.currentTimeMillis();
        return currentTime<<TIME_BIT;
    }

    private long generatorMachineCode(int machineId) {
        return machineId<<MACHINE_Bit;
    }

    private boolean checkMachineId(int machineId) {
        return machineId <= 1;
    }

    public int getDefaultMachinId() {
        return defaultMachinId;
    }

    public void setDefaultMachinId(int defaultMachinId) {
        this.defaultMachinId = defaultMachinId;
    }

    public IdGeneratorUtils(boolean isFast) {
        this.isFast = isFast;
    }

    public IdGeneratorUtils() {
    }


}

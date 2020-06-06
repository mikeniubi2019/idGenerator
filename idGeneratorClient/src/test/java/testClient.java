import com.mike.idgenerator.client.IdGeneratorClient;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.atomic.AtomicLong;

public class testClient {
    private static AtomicLong atomicLong = new AtomicLong(0);
    private static AtomicLong idLengh = new AtomicLong(0);
    private static int idLength = 500;
    private static int taskCount = 10000;
    public static void main(String[] args) throws Exception {
        int coreCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        CountDownLatch countDownLatch = new CountDownLatch(taskCount*coreCount);


        for (int i=0;i<coreCount;i++){
            IdGeneratorClient idGeneratorClient = new IdGeneratorClient();
//            idGeneratorClient.option(IdGeneratorClient.ADDRESS,"192.168.1.2")
//                    .option(IdGeneratorClient.PORT,6666);
            idGeneratorClient.build();
            executorService.submit(
                    ()->{
                        long start = System.currentTimeMillis();
                        for (int j=0;j<taskCount;j++){
                            try {
                                long[] ids = idGeneratorClient.getIds(idLength);
                                //idLengh.addAndGet(ids.length);
                                //Arrays.stream(ids).parallel().forEach(System.out::print);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }finally {
                                countDownLatch.countDown();
                            }
                           // System.out.println("请求成功一次"+(System.currentTimeMillis()-start));
                        }
                        atomicLong.addAndGet(System.currentTimeMillis()-start);
                    }
            );
        }
        countDownLatch.await();
        System.out.println(idLengh.get());
        System.out.println("生成"+idLength*taskCount*coreCount+"个id，总用时："+atomicLong.get()+"毫秒，tps为："+((idLength*taskCount*coreCount)/(atomicLong.get()/1000)));

    }
}

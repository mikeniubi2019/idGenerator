import com.mike.idgenerator.utils.IdGeneratorUtils;

import java.util.*;

public class test {
    public static void main(String[] args) {
        IdGeneratorUtils idGeneratorUtils = new IdGeneratorUtils();
        long[] longs = new long[30000000];
        int count = 0;
        long startTime = System.currentTimeMillis();
        Set<Long> set = new HashSet<>(5000000);
        while (count<30000000){
            long id = idGeneratorUtils.generator(0);
            longs[count] = id;
            count+=1;
        }
        long endTime = System.currentTimeMillis();

        Arrays.stream(longs).forEachOrdered(id-> {
            //System.out.println("id:" + id + "---machine:" + idGeneratorUtils.parseMachine(id) + "---time:" + idGeneratorUtils.parseTimeStamp(id) + "---count:" + idGeneratorUtils.parseCount(id));
            if (set.contains(id)) System.out.println("重复:"+id);
            set.add(id);
        } );
        System.out.println("所用时间为："+(endTime-startTime)+"|每秒生成："+30000000000L/(endTime-startTime));
    }
}

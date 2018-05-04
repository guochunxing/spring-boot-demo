package org.springboot.demo.redssion;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**
 * @Author: chunxing
 * @Date: 2018/5/3  下午4:28
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRedssionTest {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void test() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
         semaphore.trySetPermits(10);
        RCountDownLatch downLatch = redissonClient.getCountDownLatch("countDownLatch");
        boolean b1 = downLatch.trySetCount(10);
        IntStream.rangeClosed(0, 9).forEach(number -> {
            threadPoolExecutor.execute(() -> {
                try {
                    downLatch.countDown();
                    semaphore.acquire();
                    Thread.sleep(1000);
                    System.out.println("number：" + number);
                    System.out.println( Thread.currentThread().getName());
                    System.out.println(semaphore.availablePermits());
                    System.out.println(DateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        Thread.sleep(50000);
        semaphore.delete();
    }
}

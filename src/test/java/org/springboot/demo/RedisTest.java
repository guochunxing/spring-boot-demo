package org.springboot.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void testString() throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(1);
        Semaphore semaphore = new Semaphore(5);

        Random random = new Random();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        String zSetkey = "sort";
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    countDownLatch.countDown();
                    start.await();
                    semaphore.acquire();
                    /**/
                    for (int j = 0; j < 10000; j++) {
                        int nextInt = random.nextInt(100);
                        stringRedisTemplate.opsForZSet().add(zSetkey, "小明" + atomicInteger.toString(), nextInt);
                        int incrementAndGet = atomicInteger.incrementAndGet();
                        System.out.println(incrementAndGet);
                        if (incrementAndGet > 1000000) {
                            end.countDown();
                            return;
                        }
                    }

                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("init");
        start.countDown();
        end.await();
    }

    @Test
    public void testLen() {
        String zSetkey = "sort";
        BoundValueOperations<String, String> name = stringRedisTemplate.boundValueOps("name");
    }
}

package org.springboot.demo;

import org.springboot.demo.utils.ContextUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Worker implements Runnable {


    private AtomicInteger number = new AtomicInteger(0);
    private Random random = new Random(100);


    @Override
    public void run() {
        ReentrantLock reentrantLock = new ReentrantLock();

        String zSetkey = "sort";


        while (true) {
            double nextDouble = random.nextDouble();
            System.out.println(Thread.currentThread().getName() + "插入：" + number.toString());
            reentrantLock.lock();
            StringRedisTemplate stringRedisTemplate = ContextUtil.getBean(StringRedisTemplate.class);
            stringRedisTemplate.opsForZSet().add(zSetkey, "dongbin-" + number.toString(), nextDouble);
            reentrantLock.unlock();
            number.incrementAndGet();
            if (number.compareAndSet(10000, 10000)) {
                return;
            }
        }
    }
}

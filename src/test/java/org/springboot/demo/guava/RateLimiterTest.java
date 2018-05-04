package org.springboot.demo.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.joda.time.DateTime;
import org.springboot.demo.config.ExecutionHandler;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: chunxing
 * @Date: 2018/5/3  下午2:37
 * @Description:
 */
public class RateLimiterTest {

    public static void main(String[] args) {
        testWithRateLimiter();
    }


    public static void testWithRateLimiter() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 10000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5), new ExecutionHandler());
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

        RateLimiter limiter = RateLimiter.create(1); // 每秒不超过10个任务被提交

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.execute(() -> {
                try {
                    cyclicBarrier.await();
                    limiter.acquire();
                    System.out.println(finalI);
                    System.out.println(DateTime.now());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

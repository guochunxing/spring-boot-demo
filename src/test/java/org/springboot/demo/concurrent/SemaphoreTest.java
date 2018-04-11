package org.springboot.demo.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    countDownLatch.countDown();
                    semaphore.acquire();
                    System.out.println(semaphore.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
                semaphore.release();
            });
        }
        countDownLatch.await();
        System.out.println("init");
        threadPool.shutdown();
    }
}

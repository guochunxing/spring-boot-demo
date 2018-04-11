package org.springboot.demo.concurrent;


import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {

    private final static int GROUP_SIZE = 5;
    private final static Random RANDOM = new Random();

    private static void processOneGroup(final String groupName) throws InterruptedException {
        //等待所有人就绪
        final CountDownLatch preDownLatch = new CountDownLatch(GROUP_SIZE);
        final CountDownLatch startDownLatch = new CountDownLatch(1);
        final CountDownLatch endDownLatch = new CountDownLatch(GROUP_SIZE);

        System.out.println("=========>\n分组：" + groupName + "比赛开始：");

        for (int i = 0; i < GROUP_SIZE; i++) {
            new Thread(String.valueOf(i)) {
                public void run() {
                    preDownLatch.countDown();
                    System.out.println("我是线程组【" + groupName + "】,我是第" + this.getName() + "线程，准备就绪");
                    try {
                        startDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(Math.abs(RANDOM.nextInt()) % 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("我是线程组【" + groupName + "】,我是第" + this.getName() + "线程，执行完成");
                    endDownLatch.countDown();
                }
            }.start();
        }

        preDownLatch.await();//等待所有选手就位
        System.out.println("各就各位,预备跑！！！");
        startDownLatch.countDown();//开跑
        endDownLatch.await();//等待所有选手跑完
        System.out.println(groupName+"已跑完！");

    }

    public static void main(String[] args) throws InterruptedException {
        processOneGroup("线程组1");
        processOneGroup("线程组2");
    }

}

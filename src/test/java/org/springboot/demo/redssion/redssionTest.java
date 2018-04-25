package org.springboot.demo.redssion;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.nio.charset.Charset;

public class redssionTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Charset.defaultCharset().name());
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redisson = Redisson.create(config);
        redisson.getBitSet("count");
        RLock rLock = redisson.getLock("ownLock");
        Thread.sleep(1000);
        System.out.println(rLock.isLocked());
        rLock.unlock();
        Thread.sleep(1000);
        redisson.shutdown();
    }
}

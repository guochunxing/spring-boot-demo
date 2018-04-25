package org.springboot.demo.common.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {

    private static RedissonClient redissonClient;

    @Autowired
    public static void setRedissonClient(RedissonClient redissonClient) {
        RedisLock.redissonClient = redissonClient;
    }


    public static RLock getlock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }
}



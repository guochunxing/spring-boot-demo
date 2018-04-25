package org.springboot.demo.service.demo;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DemoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    /**
     * @return
     * @Cacheable(keyGenerator = "keyGenerator") 不能使用，如果连接出错，连服务降级都不可能
     */
    public String demoCache() throws InterruptedException {
        String demoCache = stringRedisTemplate.opsForValue().get("demoCache");
        if (StringUtils.isEmpty(demoCache)) {
            RLock lock = redissonClient.getLock("demoKey");
            if (!lock.isLocked()) {
                lock.lock();
                stringRedisTemplate.opsForValue().set("demoCache", "demoValue");
                lock.unlock();
            } else {
                demoCache();
            }
        }
        return demoCache;
    }

}

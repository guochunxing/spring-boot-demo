package org.springboot.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Transaction {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DefaultRedisScript<Boolean> distributedLockScript;

    @Test
    public void testTransaction() {

        String key = "LOCK";
        String value = UUID.randomUUID().toString();


        while (true) {
            stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public <K, V> List<Object> execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                    StringRedisTemplate operations = (StringRedisTemplate) redisOperations;
                    operations.watch(key);
                    operations.multi();
                    operations.opsForValue().setIfAbsent(key, value);
                    operations.expire(key, 3, TimeUnit.MINUTES);
                    List<Object> list = redisOperations.exec();
                    operations.unwatch();
                    return list;
                }
            });
            String reValue = stringRedisTemplate.opsForValue().get(key);
            if (value.equals(reValue)) {
                return;
            }
        }
    }

    @Test
    public void distributedLock(String lockName, String lockValue, Long seconds) {
        Boolean execute = stringRedisTemplate.execute(distributedLockScript, Collections.singletonList(lockName), lockValue, String.valueOf(seconds));
        System.out.println(execute);
    }
}

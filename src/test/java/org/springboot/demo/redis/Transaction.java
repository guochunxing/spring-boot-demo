package org.springboot.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Transaction {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testTransaction() {
        String key = "LOCK";
        String value = "value";
        List<Object> objects = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                redisOperations.multi();
                StringRedisTemplate operations = (StringRedisTemplate) redisOperations;
                operations.opsForValue().setIfAbsent(key, value);
                operations.expire(key, 3, TimeUnit.MINUTES);
                return redisOperations.exec();
            }
        });
        String str = stringRedisTemplate.opsForValue().get(key);
        System.out.println(str);
        objects.forEach(object -> {
            System.out.println(object.toString());
        });
    }
}

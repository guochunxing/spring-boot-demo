package org.springboot.demo;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springboot.demo.module.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testString() {
        stringRedisTemplate.opsForValue().set("hello", "world", 60, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
        User user = new User();
        user.setName("work");
        stringRedisTemplate.opsForHash().put("user", "id", JSON.toJSONString(user));
        System.out.println(stringRedisTemplate.opsForHash().get("user", "id"));
    }
}

package org.springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisConfig {


    @Bean("distributedLockScript")
    public DefaultRedisScript<Boolean> distributedLock() {
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        ClassPathResource resource = new ClassPathResource("script/lock.lua");
        defaultRedisScript.setLocation(resource);
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }
}
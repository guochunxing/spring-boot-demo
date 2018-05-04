package org.springboot.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RedssionConfig {

    private final Environment environment;

    @Autowired
    public RedssionConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = environment.getProperty("redis.lock.address");
        config.useSingleServer().setAddress(address)
                .setDatabase(environment.getProperty("redis.lock.database", int.class, 0));
        config.setLockWatchdogTimeout(3000);
        return Redisson.create(config);
    }
}

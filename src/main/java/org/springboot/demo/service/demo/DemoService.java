package org.springboot.demo.service.demo;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "cache")
public class DemoService {


    @Cacheable(keyGenerator = "keyGenerator")
    public String demoCache() {
        String str = "demoCache";
        System.err.println("执行了");
        return str;
    }
}

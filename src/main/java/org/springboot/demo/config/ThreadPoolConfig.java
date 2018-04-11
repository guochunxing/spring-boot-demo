package org.springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPool() {
        return new ThreadPoolExecutor(5, 10, 10000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(5), new ExecutionHandler());
    }
}

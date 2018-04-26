package org.springboot.demo.config;

import org.springboot.demo.config.getway.AccessFilter;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableZuulProxy
@Configuration
public class ZuulConfig {

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }
}

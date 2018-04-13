package org.springboot.demo.config.rabbit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }
}

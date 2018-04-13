package org.springboot.demo.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue serviceQueue() {
        return new Queue("serviceQueue");
    }
}

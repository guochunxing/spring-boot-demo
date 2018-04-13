package org.springboot.demo.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BindingConfig {
    @Bean
    Binding bindingExchangeA(Queue serviceQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(serviceQueue).to(topicExchange).with("topic.service");
    }
}

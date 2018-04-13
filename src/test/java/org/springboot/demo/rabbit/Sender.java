package org.springboot.demo.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() throws InterruptedException {
        String context = "hello ---";
        System.out.println("topic.service : " + context);
        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend("topicExchange", "topic.service", context + i);
        }
        Thread.sleep(1000);
    }
}

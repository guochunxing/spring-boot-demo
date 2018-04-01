package org.springboot.demo.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sender {





    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Test
    public void send() {
        String context = "hello " + "你好我是单对单测试";
        int i = 0;
        while (true) {
            System.out.println("单对单发送参数 : " + context);
            this.rabbitTemplate.convertAndSend("hello", context);
            i++;
            System.out.println(i);
        }
    }

}

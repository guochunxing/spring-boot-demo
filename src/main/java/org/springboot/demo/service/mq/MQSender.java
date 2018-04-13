package org.springboot.demo.service.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public String send() {
        String context = "hello " + "你好我是单对单测试";
        System.out.println("单对单发送参数 : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
        return context;
    }

}

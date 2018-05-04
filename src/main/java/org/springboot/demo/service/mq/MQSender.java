package org.springboot.demo.service.mq;

import org.springboot.demo.dao.mapper.UserMapper;
import org.springboot.demo.module.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserMapper userMapper;


    public String send() {
        String context = "hello ---";
        System.out.println("topic.service : " + context);
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("dongbin");
        user.setToken("xxxxxx");
        user.setEmail("xxxx");
        userMapper.insert(user);
        rabbitTemplate.convertAndSend("topicExchange", "topic.service", context);
        int i = 1 / 0;
        return null;
    }
}

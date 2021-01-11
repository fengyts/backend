package com.backend.rabbitmq.provide;

import com.backend.rabbitmq.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqProvider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send2FanoutTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_FANOUT_EXCHANGE,
                "", massage);
    }

    public void send2DirectTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_DIRECT_EXCHANGE,
                RabbitMqConfig.DIRECT_ROUTINGKEY, massage);
    }

    public void send2TopicTestAQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_TOPIC_EXCHANGE,
                "test.aaa", massage);
    }

    public void send2TopicTestBQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_TOPIC_EXCHANGE,
                "test.bbb", massage);
    }

}

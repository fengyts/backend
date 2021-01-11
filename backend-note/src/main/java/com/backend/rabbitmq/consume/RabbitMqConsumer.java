package com.backend.rabbitmq.consume;

import com.backend.rabbitmq.RabbitMqConfig;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqConsumer {

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitMqConfig.FANOUT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitMqConfig.TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanoutMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitMqConfig.FANOUT_QUEUE_NAME1, durable = "true"),
                                    exchange = @Exchange(value = RabbitMqConfig.TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanout1Msg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout1 message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitMqConfig.DIRECT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitMqConfig.TEST_DIRECT_EXCHANGE),
                                    key = RabbitMqConfig.DIRECT_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processDirectMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Direct message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitMqConfig.TOPIC_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = RabbitMqConfig.TEST_TOPIC_EXCHANGE, type = "topic"),
                                    key = RabbitMqConfig.TOPIC_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processTopicMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Topic message : " + msg);
    }

}

package com.backend.controller.rabbitmq;

import com.backend.rabbitmq.provide.RabbitMqProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq/")
public class RabbitMqController {

    @Autowired
    private RabbitMqProvider msgProducer;

    @GetMapping(value = "/sendFanout")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(){
        msgProducer.send2FanoutTestQueue("this is a test fanout message!");
    }

    @GetMapping(value = "/sendDirect")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendDirectMsg(){
        msgProducer.send2DirectTestQueue("this is a test direct message!");
    }

    @GetMapping(value = "/sendDirectA")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendTopicAMsg(){
        msgProducer.send2TopicTestAQueue("this is a test topic aaa message!");
    }

    @GetMapping(value = "/sendTopicB")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendTopicBMsg(){
        msgProducer.send2TopicTestBQueue("this is a test topic bbb message!");
    }

}

package com.ecommerce.order.rabbitmq.helloworld;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/rabbit/helloworld")
public class RabbitHelloWorldController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public void helloWorld() {
        rabbitTemplate.convertAndSend("HelloWorldQueue", "HelloWorld!" + LocalDateTime.now().toString());
    }
}

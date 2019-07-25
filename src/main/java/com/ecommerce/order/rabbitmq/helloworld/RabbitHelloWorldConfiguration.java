package com.ecommerce.order.rabbitmq.helloworld;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitHelloWorldConfiguration {
    @Bean
    public Queue helloWorldQueue() {
        return new Queue("HelloWorldQueue", false, false, false);
    }
}

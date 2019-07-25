package com.ecommerce.order.rabbitmq.helloworld;

import com.ecommerce.order.common.logging.AutoNamingLoggerFactory;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "HelloWorldQueue")
public class RabbitHelloWorldListener {
    private Logger logger = AutoNamingLoggerFactory.getLogger();

    @RabbitHandler
    public void receiveHelloWorld(String queueMessage) {
        logger.info("Received message:{}", queueMessage);
    }
}

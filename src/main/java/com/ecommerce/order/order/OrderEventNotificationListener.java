package com.ecommerce.order.order;

import com.ecommerce.order.common.logging.AutoNamingLoggerFactory;
import com.ecommerce.order.order.model.OrderCreatedEvent;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.ecommerce.order.order.OrderRabbitMqConfig.ORDER_RECEIVE_QUEUE;

@Component
@RabbitListener(queues = {ORDER_RECEIVE_QUEUE})
public class OrderEventListener {
    private Logger logger = AutoNamingLoggerFactory.getLogger();

    @RabbitHandler
    public void onOrderCreated(OrderCreatedEvent event) {
        logger.info("Received OrderCreatedEvent:{}", event.getOrderId());
    }

}

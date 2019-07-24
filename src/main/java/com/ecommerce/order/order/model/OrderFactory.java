package com.ecommerce.order.order.model;

import com.ecommerce.order.common.configuration.OrderProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFactory {
    private final OrderIdGenerator idGenerator;
    private final OrderProperties orderProperties;

    public OrderFactory(OrderIdGenerator idGenerator,
                        OrderProperties orderProperties) {
        this.idGenerator = idGenerator;
        this.orderProperties = orderProperties;
    }

    public Order create(List<OrderItem> items) {
        OrderId orderId = idGenerator.generate();
        String creator = orderProperties.getCreator();
        return Order.create(orderId, items, "theCreator");
    }
}

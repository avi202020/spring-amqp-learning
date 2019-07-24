package com.ecommerce.order.order.representation;

import com.ecommerce.order.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderRepresentationService {

    public OrderRepresentation toRepresentation(Order order) {
        return new OrderRepresentation(order);
    }
}

package com.ecommerce.order.order.representation;

import com.ecommerce.order.order.model.Order;
import com.ecommerce.order.order.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepresentation {
    private final String id;
    private final List<OrderItemRepresentation> items;
    private final BigDecimal totalPrice;
    private final OrderStatus status;
    private final Instant createdAt;

    public OrderRepresentation(Order order) {
        List<OrderItemRepresentation> itemRepresentations = order.getItems().stream()
                .map(orderItem -> new OrderItemRepresentation(orderItem.getProductId().toString(),
                        orderItem.getCount(),
                        orderItem.getItemPrice()))
                .collect(Collectors.toList());
        this.id = order.getId().toString();
        this.items = itemRepresentations;
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();

    }

    public static class OrderItemRepresentation {
        private final String productId;
        private final int count;
        private final BigDecimal itemPrice;

        public OrderItemRepresentation(String productId, int count, BigDecimal itemPrice) {
            this.productId = productId;
            this.count = count;
            this.itemPrice = itemPrice;
        }
    }
}

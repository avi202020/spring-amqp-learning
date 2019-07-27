package com.ecommerce.order.order.model;

public class OrderCreatedEvent {

    private String orderId;

    private OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

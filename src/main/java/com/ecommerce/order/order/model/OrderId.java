package com.ecommerce.order.order.model;

import com.ecommerce.order.common.model.AbstractId;

public class OrderId extends AbstractId {

    private OrderId() {
    }

    private OrderId(String id) {
        super(id);
    }

    public static OrderId orderId(String id) {
        return new OrderId(id);
    }

}

package com.ecommerce.order.order;

import com.ecommerce.order.order.command.CreateOrderCommand;
import com.ecommerce.order.order.model.Order;
import com.ecommerce.order.order.model.OrderFactory;
import com.ecommerce.order.order.model.OrderId;
import com.ecommerce.order.order.model.OrderItem;
import com.ecommerce.order.order.representation.OrderRepresentation;
import com.ecommerce.order.order.representation.OrderRepresentationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.order.order.model.OrderId.orderId;
import static com.ecommerce.order.order.model.ProductId.productId;

@Component
public class OrderApplicationService {
    private final OrderRepresentationService orderRepresentationService;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;

    public OrderApplicationService(OrderRepresentationService orderRepresentationService,
                                   OrderRepository orderRepository,
                                   OrderFactory orderFactory) {
        this.orderRepresentationService = orderRepresentationService;
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
    }

    @Transactional
    public OrderId createOrder(CreateOrderCommand command) {
        List<OrderItem> items = command.getItems().stream()
                .map(item -> OrderItem.create(productId(item.getProductId()),
                        item.getCount(),
                        item.getItemPrice()))
                .collect(Collectors.toList());

        Order order = orderFactory.create(items);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderRepresentation byId(String id) {
        Order order = orderRepository.byId(orderId(id));
        return orderRepresentationService.toRepresentation(order);
    }

}

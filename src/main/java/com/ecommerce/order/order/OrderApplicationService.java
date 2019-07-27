package com.ecommerce.order.order;

import com.ecommerce.order.common.logging.AutoNamingLoggerFactory;
import com.ecommerce.order.order.command.CreateOrderCommand;
import com.ecommerce.order.order.model.Order;
import com.ecommerce.order.order.model.OrderCreatedEvent;
import com.ecommerce.order.order.model.OrderFactory;
import com.ecommerce.order.order.model.OrderId;
import com.ecommerce.order.order.model.OrderItem;
import com.ecommerce.order.order.representation.OrderRepresentation;
import com.ecommerce.order.order.representation.OrderRepresentationService;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.order.order.OrderRabbitMqConfig.ORDER_PUBLISH_EXCHANGE;
import static com.ecommerce.order.order.model.OrderId.orderId;
import static com.ecommerce.order.order.model.ProductId.productId;

@Component
public class OrderApplicationService {
    private final RabbitTemplate rabbitTemplate;
    private final OrderRepresentationService orderRepresentationService;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private Logger logger = AutoNamingLoggerFactory.getLogger();

    public OrderApplicationService(OrderRepresentationService orderRepresentationService,
                                   OrderRepository orderRepository,
                                   OrderFactory orderFactory,
                                   ConnectionFactory connectionFactory,
                                   MessageConverter messageConverter) {
        this.orderRepresentationService = orderRepresentationService;
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String eventId = correlationData.getId();
            if (ack) {
                logger.info("Publish confirmed event[{}].", eventId);
            } else {
                logger.warn("Domain event[{}] is nacked while publish:{}.", eventId, cause);
            }

        });
        this.rabbitTemplate = rabbitTemplate;
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
        OrderCreatedEvent event = new OrderCreatedEvent(order.getId().toString());
        rabbitTemplate.convertAndSend(ORDER_PUBLISH_EXCHANGE,
                "order.created",
                event,
                new CorrelationData(event.getOrderId()));
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderRepresentation byId(String id) {
        Order order = orderRepository.byId(orderId(id));
        return orderRepresentationService.toRepresentation(order);
    }

}

package com.ecommerce.order.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderRabbitMqConfig {

    public static final String ORDER_PUBLISH_EXCHANGE = "order-publish-x";
    public static final String ORDER_PUBLISH_DLX = "order-publish-dlx";
    public static final String ORDER_PUBLISH_DLQ = "order-publish-dlq";

    public static final String ORDER_RECEIVE_QUEUE = "order-receive-q";
    public static final String ORDER_RECEIVE_DLX = "order-receive-dlx";
    public static final String ORDER_RECEIVE_DLQ = "order-receive-dlq";
    public static final String ORDER_RECOVER_EXCHANGE = "order-receive-recover-x";

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        messageConverter.setClassMapper(classMapper());
        return messageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        return classMapper;
    }

    //"发送方Exchange"
    @Bean
    public TopicExchange orderPublishExchange() {
        return new TopicExchange(ORDER_PUBLISH_EXCHANGE, true, false, ImmutableMap.of("alternate-exchange", ORDER_PUBLISH_DLX));
    }

    //"发送方DLX"，消息发送失败时传到该DLX
    @Bean
    public TopicExchange orderPublishDlx() {
        return new TopicExchange(ORDER_PUBLISH_DLX, true, false, null);
    }

    //"发送方DLQ"，所有发到"发送DLX"的消息都将路由到该DLQ
    @Bean
    public Queue orderPublishDlq() {
        return new Queue(ORDER_PUBLISH_DLQ, true, false, false, ImmutableMap.of("x-queue-mode", "lazy"));
    }

    //"发送方DLQ"绑定到"发送方DLX"
    @Bean
    public Binding orderPublishDlqBinding() {
        return BindingBuilder.bind(orderPublishDlq()).to(orderPublishDlx()).with("#");
    }

    //接收方的所有消息都发送到该"接收方Queue"，即"接收方queue"可以绑定多个"发送方Exchange"
    @Bean
    public Queue orderReceiveQueue() {
        ImmutableMap<String, Object> args = ImmutableMap.of(
                "x-dead-letter-exchange",
                ORDER_RECEIVE_DLX,
                "x-overflow",
                "drop-head",
                "x-max-length",
                300000,
                "x-message-ttl",
                24 * 60 * 60 * 1000);
        return new Queue(ORDER_RECEIVE_QUEUE, true, false, false, args);
    }

    //"接收方queue"绑定到"发送方exchange"
    @Bean
    public Binding orderReceiveBinding() {
        return BindingBuilder.bind(orderReceiveQueue()).to(orderPublishExchange()).with("order.#");
    }


    //"接收方DLX"，消息处理失败时传到该DLX
    @Bean
    public TopicExchange orderReceiveDlx() {
        return new TopicExchange(ORDER_RECEIVE_DLX, true, false, null);
    }


    //"接收方DLQ"，所有发到"接收DLX"的消息都将路由到该DLQ
    @Bean
    public Queue orderReceiveDlq() {
        return new Queue(ORDER_RECEIVE_DLQ, true, false, false, ImmutableMap.of("x-queue-mode", "lazy"));
    }

    //"接收方DLQ"绑定到"接收方DLX"
    @Bean
    public Binding orderReceiveDlqBinding() {
        return BindingBuilder.bind(orderReceiveDlq()).to(orderReceiveDlx()).with("#");
    }


    //"接收方恢复Exchange"，用于手动将"接收方DLQ"中的消息发到该DLX进行重试
    @Bean
    public TopicExchange orderReceiveRecoverExchange() {
        return new TopicExchange(ORDER_RECOVER_EXCHANGE, true, false, null);
    }

    //所有"接收方Queue"都绑定到"接收方恢复Exchange"
    @Bean
    public Binding orderReceiveRecoverBinding() {
        return BindingBuilder.bind(orderReceiveQueue()).to(orderReceiveRecoverExchange()).with("#");
    }


}

package com.bbbbbblack.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RemindMQConfig {
    /**
     * 交换机
     * 转发还未达到发送（过期）时间的提醒消息
     * 1.借书栏过期消息
     * 2.借书过期消息
     */
    @Bean
    public DirectExchange directExchange2() {
        return new DirectExchange(Exchanges.DELAY_EXCHANGE2);
    }

    /**
     * 队列
     * 死信队列
     */
    @Bean
    public Queue certQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key", "cert");//转发时的routing key
        return new Queue(Queues.DELAY_CERT_QUEUE, true, false, false, map);
    }

    @Bean
    public Queue confirmQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key", "confirm");//转发时的routing key
        return new Queue(Queues.DELAY_CONFIRM_QUEUE, true, false, false, map);
    }

    @Bean
    public Queue confirm2Queue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key", "confirm2");//转发时的routing key
        return new Queue(Queues.DELAY_CONFIRM2_QUEUE, true, false, false, map);
    }

    @Bean
    public Queue expireQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key", "expire");//转发时的routing key
        return new Queue(Queues.DELAY_EXPIRE_QUEUE, true, false, false, map);
    }

    /**
     * 队列交换机绑定
     */
    @Bean
    public Binding delayCertBinding(Queue certQueue, DirectExchange directExchange2) {
        return BindingBuilder.bind(certQueue).to(directExchange2).with("cert");
    }

    @Bean
    public Binding delayConfirmBinding(Queue confirmQueue, DirectExchange directExchange2) {
        return BindingBuilder.bind(confirmQueue).to(directExchange2).with("confirm");
    }

    @Bean
    public Binding delayConfirm2Binding(Queue confirm2Queue, DirectExchange directExchange2) {
        return BindingBuilder.bind(confirm2Queue).to(directExchange2).with("confirm2");
    }

    @Bean
    public Binding delayExpireBinding(Queue expireQueue, DirectExchange directExchange2) {
        return BindingBuilder.bind(expireQueue).to(directExchange2).with("expire");
    }
}

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
public class OrderConfig {
    @Bean
    public DirectExchange directExchange3() {
        return new DirectExchange(Exchanges.DELAY_EXCHANGE3);
    }

    /**
     * 队列
     * 死信队列
     */
    @Bean
    public Queue orderQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Exchanges.EXPIRE_EXCHANGE);//消息过期后转发到的交换机
        map.put("x-dead-letter-routing-key", "order");//转发时的routing key
        return new Queue(Queues.DELAY_ORDER_QUEUE, true, false, false, map);
    }

    @Bean
    public Binding delayOrderBinding(Queue orderQueue, DirectExchange directExchange3) {
        return BindingBuilder.bind(orderQueue).to(directExchange3).with("order");
    }
}

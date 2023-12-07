package com.bbbbbblack.amqp.publisher;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.domain.entity.BookOrder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class OrderPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(BookOrder order) {
        Date orderTime = order.getOrderTime();
        long millis = orderTime.getTime() - new Date().getTime() + TimeUnit.HOURS.toMillis(1);
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(millis));
            return message;
        };
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE3, "order", order, postProcessor);
    }
}

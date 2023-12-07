package com.bbbbbblack.amqp.publisher;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.domain.entity.BookCommend;
import com.bbbbbblack.utils.PushUtil;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommendPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(BookCommend commend) {
        Integer days = commend.getDays();
        Long time = PushUtil.setTime(days);
        String routingKey = PushUtil.setRoutingKey(days);
        long finalMillis = time;
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(finalMillis));
            return message;
        };
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE, routingKey, commend, postProcessor);
    }
}

package com.bbbbbblack.amqp.publisher;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.dao.CommendDao;
import com.bbbbbblack.domain.entity.BookList;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BorrowPublisher {
    @Autowired
    CommendDao commendDao;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(String bookId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", SecurityUtil.getNowUserId().toString());
        map.put("bookId", bookId);
//        long millis = TimeUnit.DAYS.toMillis(1);
        // TODO 测试时间
        long millis = TimeUnit.MINUTES.toMillis(30);
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(millis));
            return message;
        };
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE2, "cert", map, postProcessor);
    }

    public void remindExpire(BookList bookList) {
//        long millis = TimeUnit.DAYS.toMillis(7);
        //TODO 测试时间
        long millis = TimeUnit.SECONDS.toMillis(70);
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(millis));
            return message;
        };
        System.out.println("过期启动");
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE2, "confirm", bookList, postProcessor);
    }
}

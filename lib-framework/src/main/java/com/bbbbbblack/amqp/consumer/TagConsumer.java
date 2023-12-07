package com.bbbbbblack.amqp.consumer;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.amqp.Queues;
import com.bbbbbblack.domain.entity.Search;
import com.bbbbbblack.utils.IKUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TagConsumer {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    //添加群体标签
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.TAG_QUEUE1),
            exchange = @Exchange(name = Exchanges.KEYWORDS_EXCHANGE),
            key = {"tags"}
    )})
    public void addCommonTags(Map<String, Object> map) {
        System.out.println(Queues.TAG_QUEUE1);
        List<String> tags = (List<String>) map.get("record");
        for (String tag : tags) {
            redisTemplate.opsForZSet().incrementScore("common_tags", tag, 1.0);
        }
    }

    //添加个人标签
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.TAG_QUEUE2),
            exchange = @Exchange(name = Exchanges.KEYWORDS_EXCHANGE),
            key = {"tags"}
    )})
    public void addPersonalTags(Map<String, Object> map) {
        System.out.println(Queues.TAG_QUEUE2);
        String userId = (String) map.get("userId");
        List<String> tags = (List<String>) map.get("record");
        for (String tag : tags) {
            redisTemplate.opsForZSet().incrementScore("personal_tags:" + userId, tag, 1.0);
        }
    }
}

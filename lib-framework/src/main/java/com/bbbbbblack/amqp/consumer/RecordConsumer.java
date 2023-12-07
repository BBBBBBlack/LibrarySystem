package com.bbbbbblack.amqp.consumer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.amqp.Queues;
import com.bbbbbblack.domain.entity.Search;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class RecordConsumer {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    //在redis中保存搜索记录
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.RECORD_QUEUE),
            exchange = @Exchange(name = Exchanges.KEYWORDS_EXCHANGE),
            key = {"record"}
    )})
    public void addRecord(Map<String, Object> map) {
        System.out.println(Queues.RECORD_QUEUE);
        String userId = map.get("userId").toString();
        JSONObject jsonObject = JSONUtil.parseObj(map.get("record"));
        Search search = jsonObject.toBean(Search.class);
        Long size = redisTemplate.opsForList().size("query_record:" + userId);
        //最多保留50条
        if (StringUtils.hasText(search.getKeywords()) && search.getFrom() == 0) {
            if (size != null && size > 50L) {
                redisTemplate.opsForList().rightPopAndLeftPush("query_record:" + userId, search);
            } else {
                redisTemplate.opsForList().leftPush("query_record:" + userId, search);
            }
        }
    }
}

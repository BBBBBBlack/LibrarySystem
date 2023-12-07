package com.bbbbbblack.amqp.consumer;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.amqp.Queues;
import com.bbbbbblack.dao.CommendDao;
import com.bbbbbblack.domain.entity.BookCommend;
import com.bbbbbblack.domain.vo.BookVo;
import com.bbbbbblack.domain.vo.PageVo;
import com.bbbbbblack.utils.ESUtil;
import com.bbbbbblack.utils.PushUtil;
import com.getui.push.v2.sdk.api.PushApi;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CommandConsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    PushApi pushApi;
    @Autowired
    CommendDao commendDao;

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.EXPIRE_COMMEND_QUEUE),
            exchange = @Exchange(name = Exchanges.EXPIRE_EXCHANGE),
            key = {"one-day", "three-days", "five-days"}
    )})
    public void command(BookCommend commend1) throws Exception {
//        查找该用户设置的推荐频率
        BookCommend commend = commendDao.selectById(commend1.getUserId());
        System.out.println(commend);
        if (commend == null ||
                commend.getStatus() == 0 ||
                !Objects.equals(commend1.getVersion(), commend.getVersion()) ||
                commend.getClientId() == null) {
            return;
        }
        //设置推送频率
        Long time = PushUtil.setTime(commend.getDays());
        MessagePostProcessor postProcessor = message -> {
//            System.out.println(time+"+++++++++++++++++++++++");
            message.getMessageProperties().setExpiration(String.valueOf(time));
            return message;
        };
        //推送
        Set<Object> personalTags = redisTemplate.opsForZSet().reverseRange("personal_tags:" + commend1.getUserId(), 0, PageVo.pageSize);
        Set<Object> tags;
        if (personalTags != null) {
            tags = personalTags;
        } else {
            tags = redisTemplate.opsForZSet().reverseRange("common_tags", 0, PageVo.pageSize);
        }
        List<String> list = new ArrayList<>();
        assert tags != null;
        for (Object tag : tags) {
            list.add((String) tag);
        }
        List<BookVo> bookVoList = ESUtil.getMoreLikeThis("library_book", list, 0);
        System.out.println(bookVoList);
        PushUtil.pushMessage(pushApi, "书籍推荐" + commend.getDays(),
                bookVoList.toString(), commend.getClientId());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE,
                PushUtil.setRoutingKey(commend.getDays()), commend, postProcessor);
    }
}

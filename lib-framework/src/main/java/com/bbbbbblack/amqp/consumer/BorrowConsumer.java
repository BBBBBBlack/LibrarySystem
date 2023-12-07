package com.bbbbbblack.amqp.consumer;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.amqp.Queues;
import com.bbbbbblack.dao.*;
import com.bbbbbblack.domain.entity.*;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BorrowConsumer {
    @Autowired
    PushApi pushApi;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    SingleDao singleDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    CommendDao commendDao;
    @Autowired
    UserDao userDao;

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.EXPIRE_REMIND_QUEUE),
            exchange = @Exchange(name = Exchanges.EXPIRE_EXCHANGE),
            key = {"cert"}
    )})
    public void remind(Map<String, String> map) {
        //判断管理员是否确认
        String userId = map.get("userId");
        String bookId = map.get("bookId");
        String key = "bookCert:" + userId + ":" + bookId;
        //未确认且已过期
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            BookSingle single = singleDao.selectById(bookId);
            Long groupId = single.getGroupId();
            BookCommend commend = commendDao.selectById(userId);
            Book book = bookDao.selectById(groupId);
            if (commend != null) {
                String clientId = commend.getClientId();
                //推送消息
                if (clientId != null) {
                    PushUtil.pushMessage(pushApi, "借书栏过期提醒", "您id为" + bookId + "的书已过期", clientId);
                }
            }
            //修改数据书籍表状态
            singleDao.updateById(new BookSingle(Long.valueOf(bookId), groupId, 0));
            book.setAvailableNum(book.getAvailableNum() + 1);
            bookDao.updateById(book);
            //删除借书栏信息
            redisTemplate.delete(key);
            System.out.println("借书栏过期+++++++++++++");
        }
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.EXPIRE_CONFIRM_QUEUE),
            exchange = @Exchange(name = Exchanges.EXPIRE_EXCHANGE),
            key = {"confirm", "confirm2"}
    )})
    public void confirm(BookList bookList) {
        //判断是否还书
        BookList bookList2 = borrowDao.selectById(bookList.getId());
        if (bookList2.getStatus() == 2) {
            return;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(bookList.getBorrowTime().getTime() + TimeUnit.DAYS.toMillis(14));
        Long userId = bookList2.getUserId();
        BookCommend commend = commendDao.selectById(userId);
        if (commend == null || commend.getClientId() == null) {
            return;
        }

        PushUtil.pushMessage(
                pushApi,
                "借书过期提醒",
                "您id为" + bookList.getBookId() + "的书将于" + date + "过期"
                , commend.getClientId()
        );
        System.out.println("您id为" + bookList.getBookId() + "的书将于" + date + "过期");
        //剩下一天 TODO 测试时间
        long expireTime = new Date().getTime() - bookList.getBorrowTime().getTime();//借书总时长
//        if (expireTime <= TimeUnit.DAYS.toMillis(1))
        if (TimeUnit.SECONDS.toMillis(140) - expireTime <= TimeUnit.SECONDS.toMillis(10))
        {
            MessagePostProcessor postProcessor2 = message -> {
//                message.getMessageProperties().setExpiration(String.valueOf(TimeUnit.DAYS.toMillis(1)));
                message.getMessageProperties().setExpiration(String.valueOf(TimeUnit.SECONDS.toMillis(10)));
                return message;
            };
            rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE2, "expire", bookList, postProcessor2);
            return;
        }
        //三天后再提醒一遍 TODO 测试时间
        long millis = TimeUnit.SECONDS.toMillis(30);
//        long millis = TimeUnit.DAYS.toMillis(3);
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setExpiration(String.valueOf(millis));
            return message;
        };
        rabbitTemplate.convertAndSend(Exchanges.DELAY_EXCHANGE2, "confirm2", bookList, postProcessor);
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.EXPIRE_EXPIRE_QUEUE),
            exchange = @Exchange(name = Exchanges.EXPIRE_EXCHANGE),
            key = {"expire"}
    )})
    public void expire(BookList bookList) {
        //判断是否还书
        BookList bookList2 = borrowDao.selectById(bookList.getId());
        if (bookList2.getStatus() == 2) {
            return;
        }
        System.out.println("您id为" + bookList.getBookId() + "的书已过期");
//        //扣押金
        Long userId = bookList2.getUserId();
        //过期提醒
        BookCommend commend = commendDao.selectById(userId);
        if (commend == null || commend.getClientId() == null) {
            return;
        }
        PushUtil.pushMessage(
                pushApi,
                "借书过期提醒",
                "您id为" + bookList.getBookId() + "的书已过期"
                , commend.getClientId()
        );
    }
}

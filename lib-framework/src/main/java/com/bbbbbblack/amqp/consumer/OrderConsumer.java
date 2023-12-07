package com.bbbbbblack.amqp.consumer;

import com.bbbbbblack.amqp.Exchanges;
import com.bbbbbblack.amqp.Queues;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.dao.OrderDao;
import com.bbbbbblack.dao.SingleDao;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.BookOrder;
import com.bbbbbblack.domain.entity.BookSingle;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    @Autowired
    SingleDao singleDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = Queues.EXPIRE_ORDER_QUEUE),
            exchange = @Exchange(name = Exchanges.EXPIRE_EXCHANGE),
            key = {"order"}
    )})
    public void order(BookOrder order) {
        BookOrder bookOrder = orderDao.selectById(order.getId());
        //未被取走，预订过期
        if (bookOrder.getStatus() == 0) {
            BookSingle single = singleDao.selectById(bookOrder.getBookId());
            single.setStatus(0);
            singleDao.updateById(single);
            Book book = bookDao.selectById(single.getGroupId());
            book.setAvailableNum(book.getAvailableNum() + 1);
            bookDao.updateById(book);
            redisTemplate.convertAndSend("returnBook", book.getId().toString());
            order.setStatus(-1);
            orderDao.updateById(order);
        }
    }
}

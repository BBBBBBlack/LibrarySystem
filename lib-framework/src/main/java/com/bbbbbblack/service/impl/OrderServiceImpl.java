package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.amqp.publisher.OrderPublisher;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.dao.CommendDao;
import com.bbbbbblack.dao.OrderDao;
import com.bbbbbblack.dao.SingleDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.domain.entity.BookCommend;
import com.bbbbbblack.domain.entity.BookOrder;
import com.bbbbbblack.domain.entity.BookSingle;
import com.bbbbbblack.domain.vo.BookOrderVo;
import com.bbbbbblack.domain.vo.BookVo;
import com.bbbbbblack.service.OrderService;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (BookOrder)表服务实现类
 *
 * @author makejava
 * @since 2022-11-19 00:37:11
 */
@Service("bookOrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    SingleDao singleDao;
    @Autowired
    CommendDao commendDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    OrderPublisher orderPublisher;

    @Transactional
    @Override
    public Result orderBook(Long bookId, Date orderTime) {
        BookSingle single = singleDao.selectById(bookId);
        if (single == null) {
            return new Result(404, "该书不存在");
        }
        if (single.getStatus() != 0) {
            BookCommend commend = commendDao.selectById(SecurityUtil.getNowUserId());
            if (commend == null || commend.getClientId() == null) {
                return new Result(200, "该书暂时不可预订");
            } else {
                redisTemplate
                        .opsForList()
                        .rightPush("order:" + single.getGroupId(), commend.getClientId());
                return new Result(200, "该书暂时不可预订，将于有人归还时推送消息");
            }
        }
//        加入预订
        BookOrder bookOrder = new BookOrder();
        bookOrder.setBookId(bookId);
        bookOrder.setOrderTime(orderTime);
        bookOrder.setCreateTime(new Date());
        bookOrder.setUserId(SecurityUtil.getNowUserId());
        orderDao.insert(bookOrder);
//        改变副本状态
        single.setStatus(2);
        singleDao.updateById(single);
        Book book = bookDao.selectById(single.getGroupId());
        book.setAvailableNum(book.getAvailableNum() - 1);
        bookDao.updateById(book);
        //预订失效处理
        orderPublisher.sendMessage(bookOrder);
        return new Result(200, "成功预订，请在预订时间1小时内前来取书");
    }

    @Override
    public Result<List<BookOrderVo>> showOrder() {
        QueryWrapper<BookOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", SecurityUtil.getNowUserId());
        List<BookOrder> orders = orderDao.selectList(wrapper);
        List<BookOrderVo> orderVos = new ArrayList<>();
        for (BookOrder order : orders) {
            BookOrderVo o = new BookOrderVo(order);
            BookSingle single = singleDao.selectById(order.getBookId());
            Book book = bookDao.selectById(single.getGroupId());
            o.setBookVo(new BookVo(book));
            orderVos.add(o);
        }
        return new Result<>(200, "查询预订信息", orderVos);
    }
}


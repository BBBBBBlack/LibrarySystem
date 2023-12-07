package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.amqp.publisher.BorrowPublisher;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.dao.BorrowDao;
import com.bbbbbblack.dao.OrderDao;
import com.bbbbbblack.dao.SingleDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.*;
import com.bbbbbblack.domain.vo.BookListVo;
import com.bbbbbblack.service.BorrowService;
import com.bbbbbblack.utils.JwtUtil;
import com.bbbbbblack.utils.QrCodeUtils;
import com.bbbbbblack.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    BorrowPublisher borrowPublisher;
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    SingleDao singleDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    OrderDao orderDao;

    @Transactional
    @Override
    public Result addToCert(String bookId) {
        BookListVo bookListVo;
        String id;
        try {
            Claims claims = JwtUtil.parseJWT(bookId);
            id = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(200, "二维码错误");
        }
        //判断是否有书未归还
        QueryWrapper<BookList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", SecurityUtil.getNowUserId())
                .eq("status", 1);
        Integer count = borrowDao.selectCount(wrapper);
        if (count >= 2) {
            return new Result(200, "尚有书籍未归还");
        }
        //判断借书栏是否已满
        Set<Object> keys = redisTemplate.keys("bookCert:" + SecurityUtil.getNowUserId().toString() + ":*");
        if (keys != null && keys.size() >= 2) {
            return new Result(400, "借书栏已满");
        }
        //判断书籍是否可借
        BookSingle single = singleDao.selectById(id);
        if (single.getStatus() == 1) {
            return new Result(200, "该书暂不可操作");
        }
        //处理预订状态书籍
        BookOrder order;
        if (single.getStatus() == 2) {
            QueryWrapper<BookOrder> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("book_id", single.getBookId())
                    .eq("user_id", SecurityUtil.getNowUserId());
            order = orderDao.selectOne(wrapper1);
            if (order == null) {
                return new Result(200, "该书已被预订，不可借阅");
            }
            order.setStatus(1);
            orderDao.updateById(order);
        }
        Book book = bookDao.selectById(single.getGroupId());
        bookListVo = new BookListVo(book);
        bookListVo.setBookId(id);
        bookListVo.setAddTime(new Date());
        bookListVo.setStatus(0);//表示在借书栏中
        redisTemplate.opsForValue().set("bookCert:" + SecurityUtil.getNowUserId() + ":" + id, bookListVo);
        // 将借书过期信息加入消息队列中
        borrowPublisher.sendMessage(id);
        //修改副本状态和book表总数
        if (single.getStatus() != 2) {
            book.setAvailableNum(book.getAvailableNum() - 1);
            bookDao.updateById(book);
        }
        single.setStatus(1);
        singleDao.updateById(single);
        return new Result(200, "加入借书栏，请于24小时内向管理员确认，否则将不保留借书信息", bookListVo);
    }

    @Transactional
    @Override
    public Result delCert(String bookId) {
        String key = "bookCert:" + SecurityUtil.getNowUserId() + ":" + bookId;
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return new Result(200, "该书不在借书栏中");
        }
        redisTemplate.delete(key);
        BookSingle single = singleDao.selectById(Long.valueOf(bookId));
        singleDao.updateById(new BookSingle(Long.valueOf(bookId), null, 0));
        Book book = bookDao.selectById(single.getGroupId());
        Book newBook = new Book();
        newBook.setId(book.getId());
        newBook.setAvailableNum(book.getAvailableNum() + 1);
        bookDao.updateById(newBook);
        return new Result(200, "已删除");
    }

    @Override
    public Result<List<BookListVo>> showCert(String userId) {
        Set<Object> keys = redisTemplate.keys("bookCert:" + userId + ":*");
        List<BookListVo> bookListVos = new ArrayList<>();
        if (keys != null && !keys.isEmpty()) {
            for (Object key : keys) {
                BookListVo bookListVo = (BookListVo) redisTemplate.opsForValue().get(key);
                bookListVos.add(bookListVo);
            }
        }
        return new Result<>(200, "借书栏", bookListVos);
    }

    @Override
    public Result<List<BookListVo>> showBorrowed(Integer type, String userId) {

        QueryWrapper<BookList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        //type=1查询未还书籍，type=2查询已还书籍
        wrapper.eq("status", type);
        List<BookList> bookLists = borrowDao.selectList(wrapper);
        List<BookListVo> bookListVoList = new ArrayList<>();
        for (BookList bookList : bookLists) {
            bookListVoList.add(new BookListVo(bookList));
        }
        return new Result<>(200, "查询我的借书记录", bookListVoList);
    }

    @Override
    public void freshMyQR(HttpServletResponse response) {
        // TODO 测试时间
        String jwt = JwtUtil.createJWT(Secret.myWish + SecurityUtil.getNowUserId(),
                TimeUnit.MINUTES.toMillis(5));
        BufferedImage image;
        OutputStream outputStream = null;
        try {
            image = QrCodeUtils.createImage(jwt, null, false);
            response.setContentType("image/png");
            outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

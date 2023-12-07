package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.amqp.publisher.BorrowPublisher;
import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.dao.BorrowDao;
import com.bbbbbblack.dao.SingleDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.*;
import com.bbbbbblack.domain.vo.BookListVo;
import com.bbbbbblack.service.AdminBorrowService;
import com.bbbbbblack.service.BorrowService;
import com.bbbbbblack.utils.JwtUtil;
import com.bbbbbblack.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AdminBorrowServiceImpl implements AdminBorrowService {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    BorrowDao borrowDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    SingleDao singleDao;
    @Autowired
    BorrowService borrowService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    BorrowPublisher borrowPublisher;

    @Override
    public Result<List<BookListVo>> getPersonalBorrow(String userId) {
        String realId;
        try {
            Claims claims = JwtUtil.parseJWT(userId);
            String temp = claims.getSubject();
            realId = temp.substring(temp.lastIndexOf('<') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(500, "二维码过期，请刷新");
        }
        //查询借书栏书籍信息
        Result<List<BookListVo>> result = borrowService.showCert(realId);
        List<BookListVo> resultData = result.getData();
        QueryWrapper<BookList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", realId);
        List<BookList> bookLists = borrowDao.selectList(wrapper);
        if (bookLists != null && !bookLists.isEmpty()) {
            if (resultData == null) {
                resultData = new ArrayList<>();
            }
            for (BookList bookList : bookLists) {
                resultData.add(new BookListVo(bookList));
            }
        }
        return new Result<>(200, "查询用户借书信息", resultData);
    }

    @Transactional
    @Override
    public Result confirmCert(String bookId) {
        Set<Object> keys = redisTemplate.keys("bookCert:*:" + bookId);
        if (keys == null || keys.isEmpty()) {
            return new Result(500, "该书不存在于借书栏");
        }
        Object[] array = keys.toArray();
        String key = (String) array[0];
        String userId = key.substring(key.indexOf(':') + 1, key.lastIndexOf(':'));
        //查询押金是否足够
        User user = userDao.selectById(userId);
        Double rest = user.getAccountRest();
        BookSingle single = singleDao.selectById(bookId);
        Book book = bookDao.selectById(single.getGroupId());
        Double price = book.getPrice();
        if (price > rest) {
            return new Result(400, "押金不足");
        }
        //添加图书馆借书记录
        BookListVo bookListVo = (BookListVo) redisTemplate.opsForValue().get(key);
        assert bookListVo != null;
        BookList bookList = new BookList(bookListVo);
        bookList.setUserId(Long.valueOf(userId));
        bookList.setBorrowTime(new Date());
        bookList.setStatus(1);//借出未还
        borrowDao.insert(bookList);
        //删除借书栏记录
        redisTemplate.delete(key);
        //押金扣款
        user.setAccountRest(user.getAccountRest() - price);
        userDao.updateById(user);
        String key2 = "login:" + userId;
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(key2);
        if (loginUser != null) {
            loginUser.setUser(user);
            redisCache.setCacheObject(key2, loginUser);
        }
        //过期提醒发送至消息队列
        borrowPublisher.remindExpire(bookList);
        return new Result(200, "成功借阅");
    }

    @Override
    public Result returnBook(String bookId) {
        QueryWrapper<BookList> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId)
                .eq("status", 1);
        BookList list = borrowDao.selectOne(wrapper);
        if (list == null) {
            return new Result(200, "该书不处于未还状态");
        }
        //更新借书记录
        list.setStatus(2);
        list.setReturnTime(new Date());
        borrowDao.updateById(list);
        //更新馆藏书籍状态
        BookSingle single = singleDao.selectById(bookId);
        singleDao.updateById(new BookSingle(Long.valueOf(bookId), null, 0));
        Book book = bookDao.selectById(single.getGroupId());
        book.setAvailableNum(book.getAvailableNum() + 1);
        bookDao.updateById(book);
        //更新用户押金
        User user = userDao.selectById(list.getUserId());
        user.setAccountRest(Math.min(user.getAccountRest() + book.getPrice(), user.getAccount()));
        userDao.updateById(user);
        //监听还书消息
        redisTemplate.convertAndSend("returnBook", book.getId().toString());
        return new Result(200, "成功归还");
    }
}

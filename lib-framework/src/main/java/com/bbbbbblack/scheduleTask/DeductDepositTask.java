package com.bbbbbblack.scheduleTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.BorrowDao;
import com.bbbbbblack.dao.CommendDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.entity.BookCommend;
import com.bbbbbblack.domain.entity.BookList;
import com.bbbbbblack.domain.entity.LoginUser;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.utils.PushUtil;
import com.getui.push.v2.sdk.api.PushApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DeductDepositTask {

    @Autowired
    BorrowDao borrowDao;
    @Autowired
    CommendDao commendDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    PushApi pushApi;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    @Async
    @Transactional
    public void deductDeposit() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(14)));
        QueryWrapper<BookList> wrapper = new QueryWrapper<>();
        wrapper.le("borrow_time", time);
        wrapper.eq("status", 1);
        List<BookList> bookLists = borrowDao.selectList(wrapper);
        for (BookList list : bookLists) {
            Long userId = list.getUserId();
//            扣押金
            User user = userDao.selectById(userId);
            user.setAccount(user.getAccount() - 0.5);
            user.setAccountRest(user.getAccountRest() - 0.5);
            userDao.updateById(user);
//            更新redis用户押金信息
            String key = "login:" + userId;
            LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(key);
            if (loginUser != null) {
                loginUser.setUser(user);
                redisTemplate.opsForValue().set(key, loginUser);
            }
            BookCommend commend = commendDao.selectById(userId);
            if (commend == null || commend.getClientId() == null) {
                return;
            }
            PushUtil.pushMessage(pushApi, "书籍已过期",
                    "您id为" + list.getBookId() + "的书已过期,将扣款",
                    commend.getClientId());
        }
    }
}

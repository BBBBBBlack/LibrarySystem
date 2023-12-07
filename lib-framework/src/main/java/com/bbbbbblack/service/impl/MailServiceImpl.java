package com.bbbbbblack.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Mail;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.service.MailService;
import com.bbbbbblack.utils.MailUtil;
import com.bbbbbblack.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    RedisCache redisCache;
    @Value("${spring.mail.username}")
    String from;
    @Autowired
    UserDao userDao;

    @Override
    public Result sendMail(String email, Integer type) {
        String code = UUID.randomUUID().toString();
        Mail mail = new Mail(from, email, null, code);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userDao.selectOne(queryWrapper);
//        注册邮件
        if (type == 1) {
            if (user != null) {
                return new Result(200, "用户已存在");
            }
            mail.setSubject("注册邮件：请复制以下验证码（30分钟内有效）\n");
            redisCache.setCacheObject(mail.getTo(), code, 30, TimeUnit.MINUTES);
        } else if (type == 2) {
            if (user == null) {
                return new Result(200, "用户不存在");
            }
            mail.setSubject("追回密码邮件：请复制以下验证码（5分钟内有效）\n");
            redisCache.setCacheObject(mail.getTo(), code, 5, TimeUnit.MINUTES);
        }
        MailUtil.send(mail);
        return new Result(200, "邮件发送成功");
    }
}

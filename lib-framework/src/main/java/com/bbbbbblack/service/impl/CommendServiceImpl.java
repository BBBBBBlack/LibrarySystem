package com.bbbbbblack.service.impl;

import com.bbbbbblack.amqp.publisher.CommendPublisher;
import com.bbbbbblack.dao.CommendDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.BookCommend;
import com.bbbbbblack.service.CommendService;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class CommendServiceImpl implements CommendService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    CommendPublisher commendPublisher;
    @Autowired
    CommendDao commendDao;

    @Override
    public Result openPush(String clientId) {
        try {
            commendDao.insert(new BookCommend(SecurityUtil.getNowUserId(), clientId, null, null, 0));
        } catch (DuplicateKeyException e){
            return new Result(400,"推送已开启");
        }
        return new Result(200,"开启推送");
    }

    @Override
    public Result closePush() {
        commendDao.deleteById(SecurityUtil.getNowUserId());
        return new Result(200,"推送关闭");
    }

    @Override
    public Result openCommend(Integer days) {
        BookCommend commend1 = commendDao.selectById(SecurityUtil.getNowUserId());
        BookCommend commend = new BookCommend(SecurityUtil.getNowUserId(), null, days, null, 1);
        if (commend1 == null) {
            return new Result(200, "消息推送功能未开启");
        }
        if (!commend.equals(commend1)) {
            commend.setVersion(commend1.getVersion() + 1);
            commendDao.updateById(commend);
        }
        //推荐功能本来未开启
        commendPublisher.sendMessage(commend);
        return new Result(200, "开启推荐功能");
    }

    @Override
    public Result closeCommend() {
        BookCommend commend = new BookCommend(SecurityUtil.getNowUserId(), null, null, null, 0);
        commendDao.updateById(commend);
        return new Result(200, "关闭推荐功能");
    }
}

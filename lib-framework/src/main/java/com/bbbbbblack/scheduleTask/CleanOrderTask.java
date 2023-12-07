package com.bbbbbblack.scheduleTask;


import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.DepositOrderDao;
import com.bbbbbblack.domain.entity.DepositOrder;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CleanOrderTask {
    @Autowired
    DepositOrderDao depositOrderDao;

    @Scheduled(cron = "0,50 * * * * ? ")
    @Async
    @Transactional
    //删除处于未支付状态超过一天的订单
    public void cleanOrder() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss = format.format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000));
        QueryWrapper<DepositOrder> wrapper = new QueryWrapper<>();
        wrapper.le("create_time", ss);
        wrapper.eq("trade_status", 0);
        depositOrderDao.delete(wrapper);
    }
}

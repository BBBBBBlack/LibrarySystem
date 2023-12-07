package com.bbbbbblack.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bbbbbblack.dao.DepositOrderDao;
import com.bbbbbblack.dao.UserDao;
import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.DepositOrder;
import com.bbbbbblack.domain.entity.LoginUser;
import com.bbbbbblack.domain.entity.User;
import com.bbbbbblack.domain.vo.DepositOrderVo;
import com.bbbbbblack.myException.NotifyException;
import com.bbbbbblack.service.DepositOrderService;
import com.bbbbbblack.utils.AliPayUtil;
import com.bbbbbblack.utils.RedisCache;
import com.bbbbbblack.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * (DepositOrder)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 14:40:50
 */
@Service("depositOrderService")
public class DepositOrderServiceImpl implements DepositOrderService {
    @Autowired
    DepositOrderDao depositOrderDao;
    @Autowired
    UserDao userDao;
    @Autowired
    AliPayUtil aliPayUtil;
    @Autowired
    RedisCache redisCache;

    @Override
    public String addDeposit(Integer amount) {
        DepositOrder order = new DepositOrder();
        order.setSubject("增加押金");
        order.setTotalAmount(amount.doubleValue());
//        order.setUserId(SecurityUtil.getNowUserId());
        order.setCreateTime(new Date());
        depositOrderDao.InsertOrder(order);
        return aliPayUtil.pay(order);
    }

    @Transactional
    @Override
    public Result<String> refundDeposit(String outTradeNo) {
        DepositOrder order = depositOrderDao.selectById(outTradeNo);
        Double totalAmount = order.getTotalAmount();
        if (!SecurityUtil.getNowUserId().equals(order.getUserId())) {
            return new Result<>(400, "订单不存在");
        }
        AlipayResponse response;
        try {
            response = aliPayUtil.refund(totalAmount, outTradeNo);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return new Result<>(500, "退款失败，请稍后再试");
        }
        //更新订单状态
        order.setTradeStatus(-1);
        depositOrderDao.updateById(order);
        String redisKey = "login:" + order.getUserId();
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        User user = loginUser.getUser();
        user.setAccountRest(user.getAccountRest() - order.getTotalAmount());
        user.setAccount(user.getAccount() - order.getTotalAmount());
        loginUser.setUser(user);
        userDao.updateById(user);
        redisCache.setCacheObject(redisKey, loginUser);
        return new Result<>(200, "成功退款", response.getBody());
    }

    @Override
    @Transactional
    public Result payNotify(HttpServletRequest request) {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                System.out.println(name + " = " + request.getParameter(name));
            }
            //更新订单
            String outTradeNo = params.get("out_trade_no");
            DepositOrder order = depositOrderDao.selectById(outTradeNo);
            order.setTradeNo(params.get("trade_no"));
            order.setGmtPayment(params.get("gmt_payment"));
            order.setTradeStatus(1);
            depositOrderDao.updateById(order);
            //更新用户押金（redis和数据库）
            String redisKey = "login:" + order.getUserId();
            LoginUser loginUser = redisCache.getCacheObject(redisKey);
            User user = loginUser.getUser();
            user.setAccountRest(user.getAccountRest() + order.getTotalAmount());
            user.setAccount(user.getAccount() + order.getTotalAmount());
            loginUser.setUser(user);
            userDao.updateById(user);
            redisCache.setCacheObject(redisKey, loginUser);
            return new Result(200, "支付成功");
        } else {
            try {
                throw new NotifyException("回调错误");
            } catch (NotifyException e) {
                e.printStackTrace();
                return new Result(500, "支付失败");
            }

        }
    }

    @Override
    public Result<List<DepositOrderVo>> showDepositRecord() {
        QueryWrapper<DepositOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", SecurityUtil.getNowUserId())
                .eq("trade_status", 1)
                .or()
                .eq("trade_status", -1);
        List<DepositOrder> depositOrders = depositOrderDao.selectList(wrapper);
        List<DepositOrderVo> depositOrderVos = new ArrayList<>();
        for (DepositOrder order : depositOrders) {
            depositOrderVos.add(new DepositOrderVo(order));
        }
        return new Result<>(200, "查询支付押金记录", depositOrderVos);
    }
}


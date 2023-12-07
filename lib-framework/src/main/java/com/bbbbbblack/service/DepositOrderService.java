package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.DepositOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (DepositOrder)表服务接口
 *
 * @author makejava
 * @since 2022-11-08 14:40:50
 */
public interface DepositOrderService {
    String addDeposit(Integer amount);

    Result<String> refundDeposit(String outTradeNo);

    Result payNotify(HttpServletRequest request);

    Result<List<DepositOrderVo>> showDepositRecord();
}


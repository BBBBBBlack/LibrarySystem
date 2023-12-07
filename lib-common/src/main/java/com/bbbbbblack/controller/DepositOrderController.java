package com.bbbbbblack.controller;


import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.vo.DepositOrderVo;
import com.bbbbbblack.service.DepositOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (DepositOrder)表控制层
 *
 * @author makejava
 * @since 2022-11-08 14:40:44
 */
@RestController
@RequestMapping("deposit")
public class DepositOrderController {
    /**
     * 服务对象
     */
    @Autowired
    DepositOrderService depositOrderService;

    @RequestMapping("/addDeposit")
    public String addDeposit(@RequestParam Integer amount) {
        return depositOrderService.addDeposit(amount);
    }

    @PostMapping("/payNotify")
    public Result payNotify(HttpServletRequest request) {
        return depositOrderService.payNotify(request);
    }

    @GetMapping("/showDepositRecord")
    public Result<List<DepositOrderVo>> showDepositRecord() {
        return depositOrderService.showDepositRecord();
    }

    @PostMapping("/refundDeposit")
    public Result<String> refundDeposit(@RequestParam String outTradeNo) {
        return depositOrderService.refundDeposit(outTradeNo);
    }
}

